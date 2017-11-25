package com.CSU.Syx.configuration.websocketConfig;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Getter
public class MessageTrans {
    private String fromName;
    private String toName;
    private Date time;
    private String text;

    public String getToName(){
        return this.toName;
    }

    public String getFromName(){
        return this.fromName;
    }

    public String getText(){
        return this.text;
    }

    public Date getTime(){
        return this.time;
    }
}
