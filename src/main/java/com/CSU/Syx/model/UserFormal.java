package com.CSU.Syx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.awt.*;

/*
*注册用户使用
* 加入密码确定本人登陆
* 加入email，预想是发送注册邮件，确认本人注册
*/
@Entity
public class UserFormal extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String password;    //用户使用的密码
    private String email;   // 邮件地址

    public UserFormal(String name, Image image, String password, String email){
        this.setName(name);
        this.setAvatar(image);
        this.setPassword(password);
        this.setEmail(email);
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public boolean RoleCheck(){
        // 对于正式用户，返回true
        return true;
    }
}
