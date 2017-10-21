package com.CSU.Syx.model;

import javax.persistence.*;

import java.util.UUID;

/*
* 注册用户使用
* 加入密码确定本人登陆
* 加入email，预想是发送注册邮件，确认本人注册
*/
@Entity
@Table(name = "user_formal")
public class UserFormal extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uid;   // 具体id
    @Column(nullable = false)
    private String name;    // 名字
    private String avatar;   // 头像
    @Column(nullable = false)
    private String password;    //用户使用的密码
    @Column(nullable = false)
    private String email;   // 邮件地址
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "history_message")
    private message historyMessage;    // 历史消息

    public UserFormal(String name, String avatar, String password, String email) {
        this.setName(name);
        this.setAvatar(avatar);
        this.setPassword(password);
        this.setEmail(email);
        this.setUid();
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getAvatar() {
        return this.avatar;
    }

    @Override
    public UUID getUid() {
        return this.uid;
    }

    public message getHistoryMessage() {
        // 我在这里使用了historyMessage但是IDE提示并没有使用，可能有bug我没发现？
        return this.historyMessage;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setUid() {
        this.uid = UUID.randomUUID();
    }

    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void addMessage(String newMessage) {
        // 这里也使用了
        this.historyMessage.appendMessageRecord(newMessage);
    }

    @Override
    public boolean userCheck() {
        // 对于正式用户，返回true
        return true;
    }

    @Override
    public boolean roleCheck() {
        // 检查是否是客服
        return false;
    }
}
