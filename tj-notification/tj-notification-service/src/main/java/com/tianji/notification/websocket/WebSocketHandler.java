package com.tianji.notification.websocket;

import com.tianji.notification.constants.NotificationConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket 核心处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager sessionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // userId 由 WebSocketInterceptor 在握手阶段注入到 attributes
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null) {
            try { session.close(); } catch (Exception ignored) {}
            return;
        }
        sessionManager.register(userId, session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();

        // 心跳回复
        if (NotificationConstants.WS_HEARTBEAT.equals(payload)) {
            try {
                session.sendMessage(new TextMessage("{\"type\":\"HEARTBEAT\",\"time\":" +
                    System.currentTimeMillis() + "}"));
            } catch (Exception e) {
                log.error("心跳响应失败", e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = (Long) session.getAttributes().get("userId");
        sessionManager.remove(session.getId(), userId);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket传输异常 sessionId={}", session.getId(), exception);
        Long userId = (Long) session.getAttributes().get("userId");
        sessionManager.remove(session.getId(), userId);
    }

    /** 每30秒检测僵尸连接 */
    @Scheduled(fixedDelay = 30000)
    public void heartbeatCheck() {
        // WebSocket 框架自带心跳，此处做兜底检测
        log.debug("在线用户数: {}", sessionManager.getOnlineCount());
    }
}
