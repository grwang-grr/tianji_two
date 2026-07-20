package com.tianji.notification.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

/**
 * 通知发送请求 DTO（服务间调用）
 */
@Data
@ApiModel("通知发送请求")
public class NotificationDTO {

    @ApiModelProperty("通知类型")
    @NotNull(message = "通知类型不能为空")
    private Integer type;

    @ApiModelProperty("通知标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty("通知内容")
    @NotBlank(message = "内容不能为空")
    private String content;

    @ApiModelProperty("目标用户ID集合（为空则发送全员通知）")
    private Set<Long> targetUserIds;

    @ApiModelProperty("发送渠道（1=WebSocket,2=站内信,3=短信,4=邮件）")
    private Integer channel;

    @ApiModelProperty("业务关联ID（如课程ID、订单ID）")
    private Long bizId;

    @ApiModelProperty("业务类型（如course/order/question）")
    private String bizType;

    @ApiModelProperty("扩展参数（用于前端跳转路由等）")
    private Map<String, Object> extraParams;
}
