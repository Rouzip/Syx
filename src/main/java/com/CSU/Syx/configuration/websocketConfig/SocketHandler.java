package com.CSU.Syx.configuration.websocketConfig;

import com.CSU.Syx.modelRepository.MessageRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.*;

import com.CSU.Syx.model.Message;
import com.CSU.Syx.model.User;
import com.CSU.Syx.modelRepository.UserRepository;

/**
 * socket动作的具体实现
 *
 * @author Rouzip
 * @date 2017.11.6
 */
@Component
public class SocketHandler implements WebSocketHandler {
    /**
     * 用来操作数据库之中的数据的接口
     */
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    /**
     * 记录uid和session对应的map，权限是写死在数据库之中的
     */
    private static Map<String, WebSocketSession> userSessionMap;
    /**
     * 将用户和UID对应起来，在我们的预想里面只是方便提供用户list
     */
    public static Map<String, String> NameToUid;

    public static User admin = new User("4c4b716b-dbc3-47fe-b750-95dfee776647",
            "admin",
            "123",
            "rouzipking@gamil.com",
            "admin");

    // 静态初始化两个map
    static {
        userSessionMap = new HashMap<>(1024);
        NameToUid = new HashMap<>(1024);
    }

    /**
     * 判断是否存在于Map之中，若不存在则加入Map
     *
     * @param webSocketSession 接受webSocketSession作为参数
     * @throws Exception 抛出所有异常
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        String uid = (String) webSocketSession.getAttributes().get("uid");
        // 从数据库进行检索，后边异常是针对匿名用户的
        User user = userRepository.findUserById(uid);
        // 正常用户可以获得名字，匿名用户抛出异常单独设置名字
        try {
            String name = user.getName();
            userSessionMap.put(uid,webSocketSession);
        } catch (NullPointerException e) {
            userSessionMap.put(uid,webSocketSession);
        }
    }

    /**
     * 处理错误信息，将当前用户关闭然后从Map之中删除当前session
     *
     * @param webSocketSession 当前session
     * @param throwable        当前抛出的异常，不管，不处理
     * @throws Exception 抛出所有异常
     */
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.sendMessage(new TextMessage("error"));
        }
        // 从uid 和 session映射中删除
        for (Map.Entry entry : userSessionMap.entrySet()) {
            if (webSocketSession.equals(entry.getValue())) {
                String uid = (String) entry.getKey();
                userSessionMap.remove(uid);
                // 从name 和 uid映射中删除
                for (Map.Entry e : NameToUid.entrySet()) {
                    if (uid.equals(e.getValue())) {
                        NameToUid.remove(e.getKey());
                    }
                }
                System.out.println("transport error,remove the session in the map");
                break;
            }
        }
    }

    /**
     * 处理发送的消息，并将其发送给对应的人
     *
     * @param webSocketSession 用户对应的webSocket session
     * @param webSocketMessage 发送的信息
     * @throws Exception 抛出所有异常
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage.getPayloadLength() == 0) {
            return;
        }
        String originText = webSocketMessage.getPayload().toString();
        System.out.println(originText);
        // 解码消息记录
        MessageTrans msg = new Gson().fromJson(originText, MessageTrans.class);
        User fromUser = userRepository.findUserById(msg.getFromName());
        User toUser = userRepository.findUserById(msg.getToName());
        String text = msg.getText();
        Date date = new Date();
        // 构建存储的聊天记录
        Message history = new Message();
        history.setDate(date);
        history.setFromName(fromUser.getName());
        history.setToName(toUser.getName());
        history.setMessage(text);
        messageRepository.save(history);
        // 将消息发送到相对应的用户那里
        String toUid = NameToUid.get(toUser.getName());
        TextMessage textMessage = new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg));
        this.sendMessageToUser(toUid, textMessage);
    }

    /**
     * 从列表之中删除webSocketSession
     *
     * @param webSocketSession 已有的webSocketSession
     * @param closeStatus      关闭状态，默认状态，不设置
     * @throws Exception 抛出所有异常
     */
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        // 从uid 和 session映射中删除
        for (Map.Entry entry : userSessionMap.entrySet()) {
            if (webSocketSession.equals(entry.getValue())) {
                String uid = (String) entry.getKey();
                userSessionMap.remove(uid);
                // 从name 和 uid映射中删除
                for (Map.Entry e : NameToUid.entrySet()) {
                    if (uid.equals(e.getValue())) {
                        NameToUid.remove(e.getKey());
                    }
                }
                System.out.println("remove the session in the map");
                break;
            }
        }
        webSocketSession.close();
    }

    /**
     * 只是为了implement所有方法，无意义
     *
     * @return 无意义
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // 以下都为工具函数

    /**
     * 向指定uid的用户发送消息，uid为userSessionMap的键值
     *
     * @param uid     用户uid
     * @param message 发送的纯文本消息
     * @throws IOException 抛出所有异常
     */
    public void sendMessageToUser(String uid, TextMessage message) throws IOException {
        WebSocketSession session = userSessionMap.get(uid);
        if (session.isOpen() && session != null) {
            session.sendMessage(message);
        }
    }
}