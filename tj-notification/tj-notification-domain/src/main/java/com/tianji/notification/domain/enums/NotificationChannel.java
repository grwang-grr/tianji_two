package com.tianji.notification.domain.enums;

import com.tianji.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知渠道枚举
 */
@Getter
@AllArgsConstructor
public enum NotificationChannel implements BaseEnum {
    WEBSOCKET(1, "WebSocket实时推送"),
    INBOX(2, "站内信/消息箱"),
    SMS(3, "短信"),
    EMAIL(4, "邮件"),
    ;

    private final int value;
    private final String desc;
}
