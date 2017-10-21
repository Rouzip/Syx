package com.CSU.Syx.control.api;

import com.CSU.Syx.model.UserFormal;
import com.CSU.Syx.model.message;
import com.CSU.Syx.modelRepository.UserFormalRepository;
import com.CSU.Syx.modelRepository.messageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping(path = "/api/db")
public class modelControl {
    @Autowired
    private UserFormalRepository userFormalRepository;

    @Autowired
    private messageRepository messageRepository;

    @PostMapping(path = "/addUser")
    public @ResponseBody String addNewUser (@RequestParam String name,
                                            @RequestParam String email,
                                            @RequestParam String avatar,
                                            @RequestParam String password){
        UserFormal user = new UserFormal(name,avatar,password,email);
        userFormalRepository.save(user);
        return "Successful!";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<UserFormal> getAllUsers(){
        return userFormalRepository.findAll();
    }
}




















