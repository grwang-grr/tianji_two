package com.tianji.notification.api.config;

import com.tianji.common.constants.Constant;
import org.slf4j.MDC;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通知 API 自动配置 - 启用 Feign 客户端 + 请求ID转发
 */
@Configuration
@EnableFeignClients(basePackages = "com.tianji.notification.api.client")
public class NotificationApiConfig {

    @Bean
    public feign.RequestInterceptor notificationRequestInterceptor() {
        return template -> {
            String requestId = MDC.get(Constant.REQUEST_ID_HEADER);
            if (requestId != null) {
                template.header(Constant.REQUEST_ID_HEADER, requestId);
            }
            template.header(Constant.REQUEST_FROM_HEADER, Constant.FEIGN_ORIGIN_NAME);
        };
    }
}
