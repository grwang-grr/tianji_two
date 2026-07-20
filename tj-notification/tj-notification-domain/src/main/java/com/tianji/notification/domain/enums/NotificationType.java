package com.tianji.notification.domain.enums;

import com.tianji.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知类型枚举
 */
@Getter
@AllArgsConstructor
public enum NotificationType implements BaseEnum {
    SYSTEM(1, "系统通知"),
    COURSE(2, "课程通知"),
    ORDER(3, "订单通知"),
    INTERACTION(4, "互动通知"),
    POINTS(5, "积分通知"),
    LIVE(6, "直播通知"),
    ;

    private final int value;
    private final String desc;

    public static NotificationType of(Integer value) {
        if (value == null) return null;
        for (NotificationType type : values()) {
            if (type.value == value) return type;
        }
        return null;
    }
}
