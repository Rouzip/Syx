package com.CSU.Syx.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author Rouzip
 * @date 2017.11.9
 * 将所有指向/的导向index.html
 */
@Controller
public class HomeControl {
    @GetMapping("/")
    public String index(){
        return "index";
    }
}
