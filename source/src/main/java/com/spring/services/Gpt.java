package com.spring.services;

import com.spring.models.Chat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class Gpt {

    private final RestTemplate restTemplate;

    public Gpt(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${openai.chatgtp.api.url}")
    private String url;

    @Value("${openai.chatgtp.model}")
    private String model;

    @Value("${openai.chatgtp.max-completions}")
    private int completions;

    @Value("${openai.chatgtp.temperature}")
    private double temperature;

    @Value("${openai.chatgtp.max_tokens}")
    private int maxToken;

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public Chat.Response callGpt(String prompt) {
        Chat.Request request = new Chat.Request();
        request.setModel(model);
        request.setMessages(List.of(new Chat.Message("assistant", prompt)));
        request.setN(completions);
        request.setTemperature(temperature);
        request.setMax_token(maxToken);

        Chat.Response response = new Chat.Response();
        try {
            response = restTemplate.postForObject(url, request, Chat.Response.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return response;
        }
        return response;
    }
}
