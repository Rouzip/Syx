package com.CSU.Syx.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

/**
 * 标准用户模型，role自己添加，admin提前写死
 *
 * @author Rouzip
 * @date 2017.11.5
 */
@Data
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String password;
    private String email;
    private String role;

    public User(){

    }

    public User(String id, String name, String password, String email, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return this.name;
    }

    public String getRole() {
        return this.role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }
}
