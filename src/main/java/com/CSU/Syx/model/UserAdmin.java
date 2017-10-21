package com.CSU.Syx.model;

import java.util.HashSet;
import java.util.Set;

public class UserAdmin {
    private String name;
    private String password;
    private Set<User> normalUsers = new HashSet<User>();


    public UserAdmin(String name, String password) {
        this.setName(name);
        this.setPassword(password);
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public boolean usercheck() {
        return true;
    }

    public boolean roleCheck() {
        // 检查是否是客服
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}
