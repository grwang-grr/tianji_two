package com.tianji.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 通知服务配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "tj.notification")
public class NotificationProperties {

    /** WebSocket 心跳间隔（秒） */
    private int heartbeatInterval = 30;

    /** 通知历史保留天数 */
    private int historyRetentionDays = 90;

    /** 单用户最大推送队列深度 */
    private int maxPushQueuePerUser = 100;

    /** 管理员批量推送速率限制（条/秒） */
    private int batchPushRateLimit = 50;
}
