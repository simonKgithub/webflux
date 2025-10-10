package com.example.webflux.controller.user.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class UserChatController {

    /**
     * 단일 응답
     */
    @PostMapping("/oneshot")
    public Mono<간단한 스트링> oneShotChat(@RequestBody 유저의요청스트링, llaModel)
}
