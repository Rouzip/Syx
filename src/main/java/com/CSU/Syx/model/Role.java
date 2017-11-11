package com.CSU.Syx.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;


/**
 * @author Rouzip
 * @date 2017.11.8
 */
@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    private Long uid;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private User user;
    private String role;
}
