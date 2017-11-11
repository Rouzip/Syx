package com.CSU.Syx.model;

import lombok.Data;

import java.util.Date;

/**
 * @author Rouzip
 * @date 2017.11.11
 * 简化模型，使用list来承载聊天记录
 */
@Data
public class Message {
    private Date date;
    private String message;
}
