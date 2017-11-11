package com.CSU.Syx.control.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rouzip
 * @date 2017.11.11
 */
@RestController
@RequestMapping("/api")
public class RestControl {
    @GetMapping("/test")
    public String te(){
        return "saf";
    }

}
