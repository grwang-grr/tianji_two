package com.tianji.notification.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通知模板 DTO
 */
@Data
@ApiModel("通知模板")
public class NotificationTemplateDTO {

    @ApiModelProperty("模板ID")
    private Long id;

    @ApiModelProperty("模板编码")
    private String code;

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("通知类型")
    private Integer type;

    @ApiModelProperty("标题模板（支持占位符 {userId} {courseName} 等）")
    private String titleTemplate;

    @ApiModelProperty("内容模板（支持占位符）")
    private String contentTemplate;

    @ApiModelProperty("默认渠道（1=WebSocket,2=站内信,3=短信,4=邮件）")
    private Integer defaultChannel;

    @ApiModelProperty("是否启用")
    private Integer status;
}
