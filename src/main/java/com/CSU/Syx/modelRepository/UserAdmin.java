package com.CSU.Syx.modelRepository;

public class UserAdmin {
    private String name;
    private String password;

    public UserAdmin(String name, String password){
        this.setName(name);
        this.setPassword(password);
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name){
        this.name = name;
    }
}
