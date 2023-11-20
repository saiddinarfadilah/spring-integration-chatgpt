package com.spring.controllers;

import com.spring.models.Chat;
import com.spring.services.Gpt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@Slf4j
public class Route {

    private final Gpt service;

    public Route(Gpt service) {
        this.service = service;
    }

    @PostMapping(path = "/chat-gpt")
    public Chat.Response route(@RequestParam(required = false, name = "prompt") String prompt) {
        return service.callGpt(prompt);
    }
}
