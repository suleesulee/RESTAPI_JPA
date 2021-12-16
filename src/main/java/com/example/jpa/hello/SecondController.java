package com.example.jpa.hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class SecondController {

    @RequestMapping(value = "/hello-spring", method = RequestMethod.GET)
    public String helloString(){
        return "hello spring";
    }

    @GetMapping("/hello-rest")
    public String helloRest() {
        return "hello rest";
    }

    @GetMapping("/api/helloworld")
    public String helloRestApi() {
        return "hello rest api";
    }
}
