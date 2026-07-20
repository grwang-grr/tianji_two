package com.tianji.notification.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 管理端通知发送表单
 */
@Data
@ApiModel("通知发送表单")
public class NotificationFormDTO {

    @NotNull(message = "通知类型不能为空")
    @ApiModelProperty("通知类型")
    private Integer type;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty("标题")
    private String title;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("目标用户ID（空=全员推送）")
    private Long targetUserId;

    @ApiModelProperty("发送渠道")
    private Integer channel;
}
