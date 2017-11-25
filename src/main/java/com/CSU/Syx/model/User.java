package com.CSU.Syx.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.print.DocFlavor;
import java.util.Set;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Role> roles;

    public Set<Role> getRoles() {
        return this.roles;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
