package com.tianji.notification.controller;

import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.domain.R;
import com.tianji.notification.domain.dto.NotificationDTO;
import com.tianji.notification.domain.query.NotificationPageQuery;
import com.tianji.notification.domain.vo.NotificationVO;
import com.tianji.notification.service.INotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 通知控制器 - 用户端 + 内部服务调用
 */
@RestController
@RequestMapping("/notifications")
@Api(tags = "通知接口")
@RequiredArgsConstructor
public class NotificationController {

    private final INotificationService notificationService;


    @GetMapping("/page")
    @ApiOperation("分页查询我的通知")
    public R<PageDTO<NotificationVO>> queryMyNotifications(NotificationPageQuery query) {
        return R.ok(notificationService.queryUserNotifications(query));
    }

    @GetMapping("/unread-count")
    @ApiOperation("获取未读通知数量")
    public R<Map<String, Integer>> getUnreadCount() {
        return R.ok(Map.of("count", notificationService.getUnreadCount()));
    }

    @PutMapping("/read-all")
    @ApiOperation("标记所有通知为已读")
    public R<Void> markAllRead() {
        notificationService.markAllRead();
        return R.ok();
    }


    @PostMapping("/send")
    @ApiOperation("发送通知（同步，内部Feign调用）")
    public R<Void> sendNotification(@Valid @RequestBody NotificationDTO dto) {
        notificationService.sendNotification(dto);
        return R.ok();
    }

    @PostMapping("/send-async")
    @ApiOperation("发送通知（异步，内部Feign调用）")
    public R<Void> sendNotificationAsync(@Valid @RequestBody NotificationDTO dto) {
        notificationService.sendNotificationAsync(dto);
        return R.ok();
    }
}
