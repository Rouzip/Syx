package com.CSU.Syx.configuration.websocketConfig;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

/**
 * 作为传输时候的标准消息格式
 * @author Rouzip
 * @date 2017.11.18
 */
@Data
@Getter
public class Message {
    private String fromName;
    private String toName;
    private Date time;
    private String text;

    public String getToName(){
        return this.toName;
    }
}