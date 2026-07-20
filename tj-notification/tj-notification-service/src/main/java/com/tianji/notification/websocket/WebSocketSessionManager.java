package com.tianji.notification.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 会话管理器
 * 维护 userId → WebSocketSession 的映射关系（支持一个用户多端登录）
 */
@Slf4j
@Component
public class WebSocketSessionManager {

    /** userId → Set<sessionId> 映射 */
    private final ConcurrentHashMap<Long, Set<String>> userSessions = new ConcurrentHashMap<>();

    /** sessionId → WebSocketSession 映射 */
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * 注册会话
     */
    public void register(Long userId, String sessionId, WebSocketSession session) {
        sessions.put(sessionId, session);
        userSessions.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
        log.info("WebSocket连接建立 userId={} sessionId={} 当前在线={}", userId, sessionId, sessions.size());
    }

    /**
     * 移除会话
     */
    public void remove(String sessionId, Long userId) {
        sessions.remove(sessionId);
        if (userId != null) {
            Set<String> userSessionSet = userSessions.get(userId);
            if (userSessionSet != null) {
                userSessionSet.remove(sessionId);
                if (userSessionSet.isEmpty()) {
                    userSessions.remove(userId);
                }
            }
        }
        log.info("WebSocket连接断开 sessionId={} 当前在线={}", sessionId, sessions.size());
    }

    /**
     * 向指定用户的所有端推送消息
     */
    public void sendToUser(Long userId, String message) {
        Set<String> sessionIds = userSessions.get(userId);
        if (sessionIds == null || sessionIds.isEmpty()) {
            log.debug("用户 {} 无在线会话，跳过推送", userId);
            return;
        }
        TextMessage textMessage = new TextMessage(message);
        for (String sessionId : sessionIds) {
            WebSocketSession session = sessions.get(sessionId);
            if (session != null && session.isOpen()) {
                try {
                    synchronized (session) {  // WebSocketSession 不是线程安全的
                        session.sendMessage(textMessage);
                    }
                } catch (IOException e) {
                    log.error("推送消息失败 userId={} sessionId={}", userId, sessionId, e);
                }
            }
        }
    }

    /**
     * 获取在线用户数
     */
    public int getOnlineCount() {
        return userSessions.size();
    }

    /**
     * 检查用户是否在线
     */
    public boolean isOnline(Long userId) {
        Set<String> sessionIds = userSessions.get(userId);
        return sessionIds != null && !sessionIds.isEmpty();
    }
}
