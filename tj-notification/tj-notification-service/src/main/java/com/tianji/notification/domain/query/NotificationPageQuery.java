package com.tianji.notification.domain.query;

import com.tianji.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("通知分页查询")
public class NotificationPageQuery extends PageQuery {

    @ApiModelProperty("通知类型")
    private Integer type;

    @ApiModelProperty("是否已读")
    private Boolean isRead;

    @ApiModelProperty("渠道")
    private Integer channel;
}
