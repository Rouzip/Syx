package com.CSU.Syx.configuration.websocketConfig;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 实现了握手的具体动作
 * @author Rouzip
 * @date 2017.11.18
 */
public class HandShake implements HandshakeInterceptor {
    /**
     * 用于握手前，实现了用户id检测
     * 为用户
     * @param serverHttpRequest 用于第一次握手HTTP请求
     * @param serverHttpResponse 回复请求
     * @param webSocketHandler 处理webSocket
     * @param map 不懂
     * @return 返回是否握手成功
     * @throws Exception 抛出所有异常
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest){
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest)serverHttpRequest;
            HttpSession session = servletServerHttpRequest.getServletRequest().getSession(false);

            // 标记用户
            Long uid = (Long)session.getAttribute("uid");
            if (uid != null){
                map.put("uid",uid);
            }
            else {
                // 中断握手
                return false;
            }
        }
        //　继续握手
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        System.out.println("握手成功");
    }
}
