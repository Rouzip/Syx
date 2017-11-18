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
    @ManyToOne(cascade = {CascadeType.ALL})
    private User user;
    private String message;
}
