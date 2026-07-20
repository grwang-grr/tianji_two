package com.tianji.notification.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户通知设置 PO
 */
@Data
@TableName("user_notification_setting")
public class UserNotificationSetting {

    @TableId(type = IdType.INPUT)
    private Long userId;

    /** 是否接收系统通知 */
    private Boolean systemEnabled;

    /** 是否接收课程通知 */
    private Boolean courseEnabled;

    /** 是否接收订单通知 */
    private Boolean orderEnabled;

    /** 是否接收互动通知 */
    private Boolean interactionEnabled;

    /** 是否接收积分通知 */
    private Boolean pointsEnabled;

    /** 是否开启WebSocket推送 */
    private Boolean websocketEnabled;

    /** 是否开启短信推送 */
    private Boolean smsEnabled;

    /** 免打扰开始时间（HH:mm） */
    private String quietStartTime;

    /** 免打扰结束时间（HH:mm） */
    private String quietEndTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
