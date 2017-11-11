package com.CSU.Syx.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Rouzip
 * @date 2017.11.5
 * 默认用户模型，用于匿名用户登录
 * 此处不保存聊天记录，没有任何权限
 * 加入uid？但是只是用于一次性登陆，分发聊天记录
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    private String email;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "users")
    private Set<String> roles;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "users")
    private Set<Message> messages;
}
