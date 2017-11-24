package com.CSU.Syx.configuration.websocketConfig;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.CSU.Syx.model.Role;
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

    /**
     * 记录uid和session对应的map，权限是写死在数据库之中的
     */
    private static Map<String, WebSocketSession> userSessionMap;
    /**
     * 将用户和UID对应起来，在我们的预想里面只是方便提供用户list
     */
    public static Map<String, String> NameToUid;

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
        User user = userRepository.findUserById(uid);
        // 查看用户是否在线，建立uid和session的映射
        boolean ifExist = userSessionMap.get(uid) == null;
        if (ifExist) {
            userSessionMap.put(uid, webSocketSession);
        }
        // 查看是否是匿名用户
        boolean ifAnonymous = user.getName().equals("");
        if (!ifAnonymous){
            NameToUid.put(user.getName(),uid);
        }
        else {
            NameToUid.put("匿名用户"+uid,uid);
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
                for(Map.Entry e:NameToUid.entrySet()){
                    if(uid.equals(e.getValue())){
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
     * TODO：对于对应的消息格式，根据to和from发送给制定的人，并将消息存储在数据库
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
        String text = webSocketMessage.getPayload().toString();
        MessageTrans msg = new Gson().fromJson(text, MessageTrans.class);
        String toUser = msg.getToName();
        String uid = NameToUid.get(toUser);
        this.sendMessageToUser(uid,new TextMessage(text));
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
        for (String key : userSessionMap.keySet()) {
            userSessionMap.remove(key);
            break;
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

//    /**
//     * 判断是否为admin，鉴权使用
//     *
//     * @param name 用户名字
//     * @return 若为admin则true，否则false
//     */
//    public boolean isAdmin(String name) {
//        User user = userRepository.findUserByName(name);
//        Set<Role> roles = user.getRoles();
//        for (Role role : roles) {
//            if ("admin".equals(role.getRole())) {
//                return true;
//            }
//        }
//        return false;
//    }
}