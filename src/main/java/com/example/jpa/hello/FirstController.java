package com.example.jpa.hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FirstController {

    @GetMapping("/first-url")
    public void First() {

    }

    @ResponseBody
    @RequestMapping(value="/helloworld", method = RequestMethod.GET)
    public String HelloWorld() {
        return "Hello world";
    }
}
