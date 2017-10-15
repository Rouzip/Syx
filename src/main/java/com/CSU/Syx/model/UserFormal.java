package com.CSU.Syx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.UUID;

/*
*注册用户使用
* 加入密码确定本人登陆
* 加入email，预想是发送注册邮件，确认本人注册
*/
@Entity
public class UserFormal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uid;   // 具体id
    private String name;    // 名字
    private String avatar;   // 头像
    private String password;    //用户使用的密码
    private String email;   // 邮件地址

    public UserFormal(String name, String avatar,String password, String email){
        this.setName(name);
        this.setAvatar(avatar);
        this.setPassword(password);
        this.setEmail(email);
        this.setUid();
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return avatar;
    }

    public UUID getUid() {
        return this.uid;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(){
        this.uid = UUID.randomUUID();
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean RoleCheck(){
        // 对于正式用户，返回true
        return true;
    }
}
