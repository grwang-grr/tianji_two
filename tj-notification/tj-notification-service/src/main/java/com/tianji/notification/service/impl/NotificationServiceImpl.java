package com.tianji.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.notification.constants.NotificationConstants;
import com.tianji.notification.domain.dto.NotificationDTO;
import com.tianji.notification.domain.dto.NotificationFormDTO;
import com.tianji.notification.domain.enums.NotificationType;
import com.tianji.notification.domain.po.Notification;
import com.tianji.notification.domain.query.NotificationPageQuery;
import com.tianji.notification.domain.vo.NotificationVO;
import com.tianji.notification.mapper.NotificationMapper;
import com.tianji.notification.service.INotificationService;
import com.tianji.notification.websocket.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * 通知服务实现 - 核心推送逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl
        extends ServiceImpl<NotificationMapper, Notification>
        implements INotificationService {

    private final WebSocketSessionManager sessionManager;
    private final StringRedisTemplate redisTemplate;

    @Qualifier("notificationPushExecutor")
    private final ThreadPoolTaskExecutor pushExecutor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendNotification(NotificationDTO dto) {
        // 全员通知
        if (CollUtils.isEmpty(dto.getTargetUserIds())) {
            Notification record = buildNotification(dto, 0L);
            baseMapper.insert(record);
            return;
        }

        // 定向通知
        for (Long userId : dto.getTargetUserIds()) {
            Notification record = buildNotification(dto, userId);
            baseMapper.insert(record);

            // 异步 WebSocket 推送
            pushExecutor.submit(() -> pushToUser(record));
        }
    }

    @Override
    public void sendNotificationAsync(NotificationDTO dto) {
        pushExecutor.submit(() -> sendNotification(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendAdminNotification(NotificationFormDTO form) {
        NotificationDTO dto = BeanUtils.copyBean(form, NotificationDTO.class);
        if (form.getTargetUserId() != null) {
            dto.setTargetUserIds(Collections.singleton(form.getTargetUserId()));
        }
        sendNotification(dto);
    }

    @Override
    public PageDTO<NotificationVO> queryUserNotifications(NotificationPageQuery query) {
        Long userId = UserContext.getUser();
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.eq(query.getType() != null, Notification::getType, query.getType());
        wrapper.eq(query.getIsRead() != null, Notification::getIsRead, query.getIsRead());
        wrapper.orderByDesc(Notification::getCreateTime);

        Page<Notification> page = baseMapper.selectPage(query.toMpPage(), wrapper);

        int unreadCount = baseMapper.countUnreadByUserId(userId);

        return PageDTO.of(page, record -> {
            NotificationVO vo = NotificationVO.builder()
                .id(record.getId())
                .type(record.getType())
                .typeName(NotificationType.of(record.getType()) != null
                    ? NotificationType.of(record.getType()).getDesc() : "")
                .title(record.getTitle())
                .content(record.getContent())
                .isRead(record.getIsRead())
                .bizId(record.getBizId())
                .bizType(record.getBizType())
                .createTime(record.getCreateTime())
                .unreadCount(unreadCount)
                .build();
            return vo;
        });
    }

    @Override
    public int getUnreadCount() {
        return baseMapper.countUnreadByUserId(UserContext.getUser());
    }

    @Override
    public void markAllRead() {
        baseMapper.markAllReadByUserId(UserContext.getUser());
    }

    // ==================== 私有方法 ====================

    private Notification buildNotification(NotificationDTO dto, Long userId) {
        Notification record = BeanUtils.copyBean(dto, Notification.class);
        record.setUserId(userId > 0 ? userId : null);
        record.setIsRead(false);
        record.setIsPushed(false);
        record.setChannel(dto.getChannel() != null ? dto.getChannel() : 1);
        return record;
    }

    private void pushToUser(Notification record) {
        try {
            String message = buildWsMessage(record);
            sessionManager.sendToUser(record.getUserId(), message);

            // 更新推送状态
            lambdaUpdate()
                .set(Notification::getIsPushed, true)
                .set(Notification::getPushTime, LocalDateTime.now())
                .eq(Notification::getId, record.getId())
                .update();
        } catch (Exception e) {
            log.error("WebSocket推送失败 userId={}", record.getUserId(), e);
        }
    }

    private String buildWsMessage(Notification record) {
        return String.format(
            "{\"type\":\"%s\",\"id\":%d,\"title\":\"%s\",\"content\":\"%s\",\"time\":\"%s\"}",
            NotificationConstants.WS_NOTIFICATION, record.getId(),
            record.getTitle(), record.getContent(),
            record.getCreateTime().toString()
        );
    }
}
