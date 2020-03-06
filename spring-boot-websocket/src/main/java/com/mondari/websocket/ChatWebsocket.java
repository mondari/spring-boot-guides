package com.mondari.websocket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author limondar
 */
@Slf4j
@ServerEndpoint("/chat/{username}")
@Controller
public class ChatWebsocket {

    /**
     * 在线人数
     */
    private static int onlineCount = 0;
    /**
     * 在线客户端，Key为SessionId
     */
    private static Map<String, ChatWebsocket> clients = new ConcurrentHashMap<>();

    private Session session;
    private String username;

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {

        this.username = username;
        this.session = session;

        addOnlineCount();
        clients.put(session.getId(), this);
        log.info("有新连接加入，ID：{}，当前在线人数为{}人", session.getId(), getOnlineCount());
    }

    @OnClose
    public void onClose() {
        clients.remove(username);
        subOnlineCount();
        log.info("有一连接关闭，ID：{}，当前在线人数为{}人", session.getId(), getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message) {
        sendMessageTo("消息发送成功", session.getId());
        sendMessageAll("客户端 " + session.getId() + " 发送一条消息：" + message);
    }

    @OnError
    public void onError(Throwable error) {
        log.error("客户端 {} 的连接发生错误 {}", username, error);
    }

    /**
     * 发送消息给指定的人
     *
     * @param message
     * @param sessionId
     */
    @SneakyThrows
    private void sendMessageTo(String message, String sessionId) {
        if (clients.containsKey(sessionId)) {
            clients.get(sessionId).session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 群发消息
     *
     * @param message
     */
    private void sendMessageAll(String message) {
        for (ChatWebsocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        ChatWebsocket.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        ChatWebsocket.onlineCount--;
    }

    public static synchronized Map<String, ChatWebsocket> getClients() {
        return clients;
    }
}
