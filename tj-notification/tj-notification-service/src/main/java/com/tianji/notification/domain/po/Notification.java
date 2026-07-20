package com.tianji.notification.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知记录 PO - 映射 notification 表
 */
@Data
@TableName("notification")
public class Notification {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 通知类型：1=系统,2=课程,3=订单,4=互动,5=积分,6=直播 */
    private Integer type;

    /** 通知标题 */
    private String title;

    /** 通知内容 */
    private String content;

    /** 发送渠道：1=WebSocket,2=站内信,3=短信,4=邮件 */
    private Integer channel;

    /** 目标用户ID */
    private Long userId;

    /** 业务关联ID */
    private Long bizId;

    /** 业务类型 */
    private String bizType;

    /** 扩展参数 JSON */
    private String extraParams;

    /** 是否已读 */
    private Boolean isRead;

    /** 已读时间 */
    private LocalDateTime readTime;

    /** 是否已推送 */
    private Boolean isPushed;

    /** 推送时间 */
    private LocalDateTime pushTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long creater;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;

    @TableLogic
    private Boolean deleted;
}
