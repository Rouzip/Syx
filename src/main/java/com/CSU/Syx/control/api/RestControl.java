package com.CSU.Syx.control.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CSU.Syx.model.User;
import com.CSU.Syx.modelRepository.UserRepository;
import static com.CSU.Syx.configuration.websocketConfig.SocketHandler.NameToUid;

import java.util.List;
import java.util.Map;

/**
 * @author Rouzip2
 * @date 2017.11.11
 */
@RestController
@RequestMapping("/api")
public class RestControl {
    @Autowired
    public UserRepository userRepository;

    @GetMapping("/test")
    public Iterable te(){
        Iterable<User> result = userRepository.findAll();
        return result;
    }

    @GetMapping("/list")
    public Map userList(){
        System.out.println(NameToUid);
        return NameToUid;
    }
}
