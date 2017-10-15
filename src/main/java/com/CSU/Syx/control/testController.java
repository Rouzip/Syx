package com.CSU.Syx.control;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class testController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/hello")
    public Test test(@RequestParam(value = "name", defaultValue = "world") String name) {
        return new Test(counter.incrementAndGet(),
                String.format(template, name));
    }
}

class Test {
    private String name;
    private long age;

    public Test(long age, String name) {
        this.age = age;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public long getAge() {
        return this.age;
    }
}
