package com.lego.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;


@RestController
public class IndexController {

    @RequestMapping("/")
    public Mono<Void> index(ServerHttpResponse response) {
        return Mono.fromRunnable(() -> {
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().setLocation(URI.create("./index.html"));
        });
    }
}
