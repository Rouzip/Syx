package com.CSU.Syx.configuration.websocketConfig;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.CSU.Syx.model.User;
import com.CSU.Syx.modelRepository.UserRepository;

/**
 * 实现了握手的具体动作
 *
 * @author Rouzip
 * @date 2017.11.18
 */
public class HandShake implements HandshakeInterceptor {
    @Autowired
    private UserRepository userRepository;

    /**
     * 用于握手前，实现了用户id检测
     * 为用户制定好UID
     *
     * @param serverHttpRequest  用于第一次握手HTTP请求
     * @param serverHttpResponse 回复请求
     * @param webSocketHandler   处理webSocket
     * @param map                不懂
     * @return 返回是否握手成功
     * @throws Exception 抛出所有异常
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        System.out.println("握手成功");
    }
}
