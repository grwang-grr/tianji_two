package com.tianji.notification.mq;

import com.tianji.notification.constants.NotificationConstants;
import com.tianji.notification.domain.dto.NotificationDTO;
import com.tianji.notification.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 通知事件监听器 - 消费来自其他微服务的通知事件
 *
 * 其他服务通过 MQ 发送通知后，本监听器自动创建通知记录并推送
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final INotificationService notificationService;

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = NotificationConstants.NOTIFICATION_QUEUE, durable = "true"),
        exchange = @Exchange(name = NotificationConstants.NOTIFICATION_EXCHANGE,
                            type = "topic"),
        key = NotificationConstants.NOTIFICATION_KEY
    ))
    public void onNotificationEvent(NotificationDTO dto) {
        if (dto == null) {
            log.warn("收到空通知事件，忽略");
            return;
        }
        log.info("收到通知事件 type={} title={}", dto.getType(), dto.getTitle());
        try {
            notificationService.sendNotificationAsync(dto);
        } catch (Exception e) {
            log.error("处理通知事件失败", e);
            throw e; // 抛异常触发 MQ 重试
        }
    }
}
