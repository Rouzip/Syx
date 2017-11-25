package com.CSU.Syx.control.api;

import java.util.*;

import com.CSU.Syx.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.CSU.Syx.model.User;
import com.CSU.Syx.modelRepository.UserRepository;

import javax.validation.constraints.Null;

import static com.CSU.Syx.configuration.websocketConfig.SocketHandler.NameToUid;


/**
 * @author Rouzip2
 * @date 2017.11.11
 */
@RestController
@RequestMapping("/api")
public class RestControl {
    private static List alives = new ArrayList();

    @Autowired
    public UserRepository userRepository;

    /**
     * 管理员权限，使用cookie来进行鉴权，写死在代码里
     * TODO：添加鉴权
     * @return
     */
    @GetMapping("/list")
    public Map userList() {
        return NameToUid;
    }

    /**
     * 注册用户，具体协议实现见api文档
     * TODO：添加返回cookie
     *
     * @param userName 用户申请的名字
     * @param password 用户申请的密码
     * @param email    用户申请的邮箱
     * @return 返回文档定好的消息协议
     */
    @PostMapping("/signup")
    public Map signup(@RequestParam(value = "username") String userName,
                      @RequestParam(value = "password") String password,
                      @RequestParam(value = "email") String email) {
        Map<String, String> response = new HashMap(5);
        User user = userRepository.findUserByName(userName);
        try {
            // 此处为在数据库之中找到了该用户，返回错误信息
            String name = user.getName();
            if (name != null) {
                response.put("msg", "已有该用户");
            }
        } catch (NullPointerException e) {
            // 此处为为用户新注册，返回新生成的uuid
            User newUser = new User();
            newUser.setName(userName);
            newUser.setPassword(password);
            newUser.setEmail(email);
            Set<Role> roles = new HashSet();
            Role role = new Role();
            role.setRole("user");
            role.setUser(newUser);
            roles.add(role);
            newUser.setRoles(roles);
            // 为用户创建唯一的uuid
            String uuid = UUID.randomUUID().toString();
            response.put("uid", uuid);
            newUser.setId(uuid);
            // 将数据存入数据库
            userRepository.save(newUser);
        }
        return response;
    }

    /**
     * TODO：登陆用户，返回UUID和isAdmin
     * @return 返回结果列表
     */
    public Map login(){

    }

    /**
     * TODO：退出登陆，从列表之中删除
     */
    public void logout(){

    }

    /**
     * TODO：鉴权
     */
    public Map auth(){

    }

    public void
}
