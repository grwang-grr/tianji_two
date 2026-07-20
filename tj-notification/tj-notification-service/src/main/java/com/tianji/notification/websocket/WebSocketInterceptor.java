package com.tianji.notification.websocket;

import com.tianji.auth.common.constants.JwtConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket 握手拦截器 - 从请求中提取用户身份
 */
@Slf4j
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 从请求头获取网关注入的 user-info（JWT 已由 Gateway 验证）
        String userIdStr = request.getHeaders()
                .getFirst(JwtConstants.USER_HEADER);
        if (userIdStr == null) {
            // 备选：从 query parameter 获取
            String query = request.getURI().getQuery();
            if (query != null && query.contains("userId=")) {
                userIdStr = query.split("userId=")[1].split("&")[0];
            }
        }
        if (userIdStr != null) {
            attributes.put("userId", Long.valueOf(userIdStr));
            return true;
        }
        log.warn("WebSocket握手失败：无法获取用户身份");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
