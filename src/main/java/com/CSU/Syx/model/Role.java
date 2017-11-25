package com.CSU.Syx.model;

import lombok.Data;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne(cascade = {CascadeType.ALL})
    private User user;
    private String role;

    public String getRole() {
        return this.role;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRole(String role){
        this.role = role;
    }
}
