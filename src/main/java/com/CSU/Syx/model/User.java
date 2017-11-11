package com.CSU.Syx.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Rouzip
 * @date 2017.11.5
 * 默认用户模型，用于匿名用户登录
 * 此处不保存聊天记录，没有任何权限
 * 加入uid？但是只是用于一次性登陆，分发聊天记录
 */
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String password;
    private String email;
    private List<String> roles;
    private ArrayList<Message> messages;
}
