package com.tianji.notification.constants;

/**
 * 通知服务常量
 */
public interface NotificationConstants {

    /** Redis WebSocket 会话映射 key 前缀 */
    String WS_SESSION_PREFIX = "ws:session:";

    /** Redis 用户未读计数 key 前缀 */
    String UNREAD_COUNT_PREFIX = "notify:unread:";

    /** WebSocket 心跳消息类型 */
    String WS_HEARTBEAT = "HEARTBEAT";

    /** WebSocket 通知消息类型 */
    String WS_NOTIFICATION = "NOTIFICATION";

    /** WebSocket 认证消息类型 */
    String WS_AUTH = "AUTH";

    /** MQ 通知队列 */
    String NOTIFICATION_QUEUE = "notification.event.queue";

    /** MQ 通知交换机 */
    String NOTIFICATION_EXCHANGE = "notification.topic";

    /** MQ 通知路由键 */
    String NOTIFICATION_KEY = "notification.event";
}
