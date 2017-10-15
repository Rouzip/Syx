package com.CSU.Syx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.awt.*;
import java.util.UUID;

/*
* 默认用户模型，用于匿名用户登录
* 此处不保存聊天记录，没有任何权限
* 加入uid？但是只是用于一次性登陆，分发聊天记录
*/
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uid;   // 具体id
    private String name;    // 名字
    private String avatar;   // 头像


    public User() {
    }

    public User(String name, String avatar) {
        this.setName(name);
        this.setUid();
        this.setAvatar(avatar);
    }

    public void setName(String name) {
        this.name = name;
    }

    //设置用户头像
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public void setUid(){
        this.uid = UUID.randomUUID();
    }


    public String getName() {
        return this.name;
    }

    public UUID getUid() {
        return this.uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean RoleCheck(){
        // 检查是否是正式用户
        return false;
    }

}
