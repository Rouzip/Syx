package com.CSU.Syx.model;

import javax.persistence.*;

/*
* 这是为了记录消息的模型
* 同时可以让客服和登陆用户查到他们的聊天记录
* 但是这里调出的时候需要注意，图片是以地址的形式储存在数据库中的
* 转换成什么形式？
*/
@Entity
public class message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @OneToOne(mappedBy = "messages")
    private UserFormal user;
    private String messageRecord;

    public message(UserFormal user) {
        this.user = user;
        this.messageRecord = "";
    }

    public String getMessageRecord() {
        return messageRecord;
    }

    public void setMessageRecord(String messageRecord) {
        this.messageRecord = messageRecord;
    }

    public void appendMessageRecord(String newMessage) {
        this.messageRecord += newMessage;
    }
}