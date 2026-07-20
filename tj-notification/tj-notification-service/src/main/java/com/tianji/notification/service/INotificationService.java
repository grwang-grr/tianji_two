package com.tianji.notification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.notification.domain.dto.NotificationDTO;
import com.tianji.notification.domain.dto.NotificationFormDTO;
import com.tianji.notification.domain.po.Notification;
import com.tianji.notification.domain.query.NotificationPageQuery;
import com.tianji.notification.domain.vo.NotificationVO;

/**
 * 通知服务接口
 */
public interface INotificationService extends IService<Notification> {

    /** 发送通知（外部服务调用） */
    void sendNotification(NotificationDTO dto);

    /** 发送通知（异步） */
    void sendNotificationAsync(NotificationDTO dto);

    /** 管理端发送通知 */
    void sendAdminNotification(NotificationFormDTO form);

    /** 查询用户通知分页 */
    PageDTO<NotificationVO> queryUserNotifications(NotificationPageQuery query);

    /** 获取未读数量 */
    int getUnreadCount();

    /** 标记全部已读 */
    void markAllRead();
}
