package com.example.orderservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "order service test~~~~~~";
    }

    @GetMapping("/getdata")
    public OrderDTO getdata() {
        return new OrderDTO("bts", "방탄", "서울", 10000);
    }
}
