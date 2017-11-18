package com.CSU.Syx.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * 标准用户模型，role自己添加，admin提前写死
 * @author Rouzip
 * @date 2017.11.5
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    private String email;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private Set<Role> roles;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private Set<Message> messages;
}
