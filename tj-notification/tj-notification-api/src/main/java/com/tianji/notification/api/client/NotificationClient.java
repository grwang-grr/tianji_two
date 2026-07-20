package com.tianji.notification.api.client;

import com.tianji.notification.domain.dto.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 通知服务 Feign 客户端 - 供其他微服务调用
 */
@FeignClient(name = "notification-service")
public interface NotificationClient {

    /**
     * 发送通知（同步，用于重要通知）
     */
    @PostMapping("/notifications/send")
    void sendNotification(@RequestBody NotificationDTO dto);

    /**
     * 发送通知（异步，返回即成功）
     */
    @PostMapping("/notifications/send-async")
    void sendNotificationAsync(@RequestBody NotificationDTO dto);
}
