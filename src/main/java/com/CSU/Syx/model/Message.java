package com.CSU.Syx.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Rouzip
 * @date 2017.11.11
 */
@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    private Long id;

    private Date date;
    private String toName;
    private String fromName;
    private String message;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(Date time) {
        this.date = time;
    }

    public void setToName(String name) {
        this.toName = name;
    }

    public void setFromName(String name) {
        this.fromName = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "发送者：" + this.fromName + "    " + "接收者：" + this.toName + "    " +
                this.date + "/n/r" + this.message + "/n/r";
    }
}
