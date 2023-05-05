package com.room.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class UploadTestController {

    @GetMapping("/uploadEx")
    public void uploadEx(){

    }
}
