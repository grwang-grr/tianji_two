package com.tianji.notification.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知视图对象
 */
@Data
@Builder
@ApiModel("通知视图")
public class NotificationVO {

    @ApiModelProperty("通知ID")
    private Long id;

    @ApiModelProperty("通知类型")
    private Integer type;

    @ApiModelProperty("类型名称")
    private String typeName;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("是否已读")
    private Boolean isRead;

    @ApiModelProperty("业务ID")
    private Long bizId;

    @ApiModelProperty("业务类型")
    private String bizType;

    @ApiModelProperty("扩展参数")
    private Object extraParams;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("未读数量")
    private Integer unreadCount;
}
