package com.CSU.Syx.control.api;

import java.util.*;
import javax.print.attribute.standard.MediaSize;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.CSU.Syx.model.User;
import com.CSU.Syx.modelRepository.UserRepository;

import static com.CSU.Syx.configuration.websocketConfig.SocketHandler.NameToUid;
import static com.CSU.Syx.configuration.websocketConfig.SocketHandler.admin;


/**
 * @author Rouzip2
 * @date 2017.11.11
 */
@RestController
@RequestMapping("/api")
public class RestControl {
    /**
     * 自己定义的已经在线异常
     */
    class OnlineException extends Exception {
    }

    /**
     * 用来储存cookie
     */
    private static Map<String, String> alives = new HashMap<>();
    private String adminCookie;

    @Autowired
    public UserRepository userRepository;

    /**
     * 启用bcrypt加密算法
     */
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 鉴权之后返回对应的列表
     * 管理员权限，使用cookie来进行鉴权，写死在代码里
     *
     * @return Map name uid列表
     */
    @GetMapping("/userlist")
    public Map userList(@CookieValue(value = "auth") String auth) {
        Map userList = new HashMap(10);
        if (auth.equals(adminCookie)) {
            // 如果是admin，返回的列表没有自己
            for (Map.Entry entry : NameToUid.entrySet()) {
                // 不是admin的添加到返回列表
                if (!entry.getKey().equals("admin")) {
                    userList.put(entry.getKey(), entry.getValue());
                }
            }
        } else {
            for (Map.Entry entry:alives.entrySet()){
                if (entry.getValue().equals(auth)){
                    // 如果是普通用户，返回的只有admin和其uuid
                    userList.put("admin", admin.getId());
                    return userList;
                }
            }
            // 对于错误的cookie返回错误
            userList.put("msg","未登陆或错误cookie");
        }
        return userList;
    }

    /**
     * 注册用户，具体协议实现见api文档
     *
     * @param userName 用户申请的名字
     * @param password 用户申请的密码
     * @param email    用户申请的邮箱
     * @return 返回文档定好的消息协议
     */
    @PostMapping("/signup")
    public Map signUp(@RequestParam(value = "username") String userName,
                      @RequestParam(value = "password") String password,
                      @RequestParam(value = "email") String email,
                      HttpServletRequest httpServletRequest,
                      HttpServletResponse httpServletResponse) {
        Map response = new HashMap(5);
        User user = userRepository.findUserByName(userName);
        try {
            // 此处为在数据库之中找到了该用户，返回错误信息
            String name = user.getName();
            if (name != null) {
                response.put("msg", "已有该用户");
            }
            String admin = "admin";
            if (name.equals(admin)) {
                response.put("msg", "不能创建admin");
            }
        } catch (NullPointerException e) {
            // 对密码进行加密
            String hashPassword = passwordEncoder.encode(password);
            // 此处为为用户新注册，返回新生成的uuid
            User newUser = new User();
            newUser.setName(userName);
            newUser.setPassword(hashPassword);
            newUser.setEmail(email);
            newUser.setRole("user");
            // 为用户创建唯一的uuid
            String uuid = UUID.randomUUID().toString();
            response.put("uid", uuid);
            newUser.setId(uuid);
            // 将数据存入数据库
            userRepository.save(newUser);
            String cookeValue = UUID.randomUUID().toString();
            alives.put(userName, cookeValue);
            Cookie cookie = new Cookie("auth", cookeValue);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            NameToUid.put(userName, uuid);
        }
        response.put("isAdmin", false);
        return response;
    }

    /**
     * 登陆用户，返回UUID和isAdmin
     *
     * @return 返回结果列表
     */
    @PostMapping("/login")
    public Map login(@RequestParam("name") String name,
                     @RequestParam("password") String password,
                     HttpServletResponse httpServletResponse) {
        Map response = new HashMap(5);
        User user = userRepository.findUserByName(name);
        try {
            boolean ifCan = passwordEncoder.matches(user.getPassword(), password);
            // 验证密码正确，返回UUID和isAdmin，否则返回错误信息
            if (ifCan || password.equals(admin.getPassword())) {
                // 遍历在线名单，如果已在线抛出自定义异常
                for (Map.Entry tmp : NameToUid.entrySet()) {
                    if (tmp.getKey().equals(name)) {
                        throw new OnlineException();
                    }
                }
                response.put("uid", user.getId());
                String passName = user.getName();
                // 判断是否是admin，返回相应的判断
                if ("admin".equals(passName)) {
                    response.put("isAdmin", true);
                    String cookieValue = UUID.randomUUID().toString();
                    Cookie cookie = new Cookie("auth", cookieValue);
                    alives.put("admin",cookieValue);
                    adminCookie = cookieValue;
                    httpServletResponse.addCookie(cookie);
                    NameToUid.put("admin", admin.getId());
                } else {
                    response.put("isAdmin", false);
                    String cookieValue = UUID.randomUUID().toString();
                    Cookie cookie = new Cookie("auth", cookieValue);
                    alives.put(user.getName(), cookieValue);
                    httpServletResponse.addCookie(cookie);
                    NameToUid.put(user.getName(), user.getId());
                }
            } else {
                response.put("msg", "密码错误");
            }
        } catch (NullPointerException e) {
            response.put("msg", "查无此人");
        } catch (OnlineException o) {
            response.put("msg", "此人已在线");
        }
        return response;
    }

    /**
     * 退出登陆，从列表之中删除，是否断开连接前端决定
     */
    @GetMapping("/logout")
    public void logout(@CookieValue(value = "auth") String cookie) {
        for (Map.Entry entry : alives.entrySet()) {
            if (cookie.equals(entry.getValue())) {
                alives.remove(entry.getKey());
                NameToUid.remove(entry.getKey());
            }
        }
    }

    /**
     * 鉴权，即实现rememberme功能
     */
    @GetMapping("/auth")
    public Map auth(@CookieValue("auth")String auth){
        Map response = new HashMap(4);
        for (Map.Entry entry:alives.entrySet()){
            if (entry.getValue().equals(auth)){
                response.put("isLogged",true);
                String userName = (String) entry.getKey();
                response.put("uid",NameToUid.get(userName));
                if (userName.equals("admin")){
                    response.put("isAdmin",true);
                    response.put("username","admin");
                }
                else {
                    response.put("isAdmin",false);
                    response.put("username",userName);
                }
            }
        }
        response.put("lsLogged",false);
        return response;
    }
}
