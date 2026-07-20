package com.tianji.notification;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 通知服务 - 实时消息推送 + 多渠道通知
 * 端口: 8095 | 服务名: notification-service
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.tianji.notification.mapper")
@EnableScheduling
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
        log.info("===========================================");
        log.info("  通知服务启动成功 (notification-service)");
        log.info("  本地访问: http://localhost:8095");
        log.info("  Swagger:  http://localhost:8095/doc.html");
        log.info("===========================================");
    }
}
