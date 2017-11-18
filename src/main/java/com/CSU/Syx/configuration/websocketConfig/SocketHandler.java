package com.CSU.Syx.configuration.websocketConfig;

import com.CSU.Syx.model.Role;
import com.CSU.Syx.model.User;
import com.CSU.Syx.modelRepository.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * socket动作的具体实现
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
    private static final Map<Long,WebSocketSession> userSessionMap;
    static {
        userSessionMap = new HashMap<>();
    }

    /**
     * 判断是否存在于Map之中，若不存在则加入Map
     * @param webSocketSession 接受webSocketSession作为参数
     * @throws Exception 抛出所有异常
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        Long uid = (Long)webSocketSession.getAttributes().get("uid");
        boolean ifExist = userSessionMap.get(uid) == null;
        if (ifExist){
            userSessionMap.put(uid,webSocketSession);
        }
    }

    /**
     * 处理错误信息，将当前用户关闭然后从Map之中删除当前session
     * @param webSocketSession 当前session
     * @param throwable 当前抛出的异常，不管，不处理
     * @throws Exception 抛出所有异常
     */
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()){
            webSocketSession.close();
        }

        for(Map.Entry<Long,WebSocketSession> it : userSessionMap.entrySet()){
            if(it.getValue().getId().equals(webSocketSession.getId())){
                userSessionMap.remove(it.getKey());
                System.out.println("remove the session in the map");
                break;
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage.getPayloadLength() == 0){
            return;
        }
        Message msg = new Gson().fromJson(webSocketMessage.getPayload().toString(),Message.class);
        String toUser = msg.getToName();
    }

    /**
     * 从列表之中删除webSocketSession
     * @param webSocketSession 已有的webSocketSession
     * @param closeStatus 关闭状态，默认状态，不设置
     * @throws Exception 抛出所有异常
     */
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        for(Map.Entry<Long,WebSocketSession> it : userSessionMap.entrySet()){
            userSessionMap.remove(it.getKey());
            System.out.println("remove the session in the map");
            break;
        }
        webSocketSession.close();
    }

    /**
     * 只是为了implement所有方法，无意义
     * @return 无意义
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // 以下都为工具函数
    /**
     * 向指定uid的用户发送消息，uid为userSessionMap的键值
     * @param uid 用户uid
     * @param message 发送的纯文本消息
     * @throws IOException 抛出所有异常
     */
    public void sendMessageToUser(Long uid,TextMessage message) throws IOException{
        WebSocketSession session = userSessionMap.get(uid);
        if (session.isOpen() && session != null){
            session.sendMessage(message);
        }
    }

    /**
     * 判断是否为admin，鉴权使用
     * @param name 用户名字
     * @return 若为admin则true，否则false
     */
    public boolean isAdmin(String name){
        User user = userRepository.findUsersByName(name);
        Set<Role> roles = user.getRoles();
        for(Role role:roles){
            if ("admin".equals(role.getRole())){
                return true;
            }
        }
        return false;
    }
}