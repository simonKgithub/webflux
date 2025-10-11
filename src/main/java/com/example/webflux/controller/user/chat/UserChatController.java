package com.example.webflux.controller.user.chat;

import com.example.webflux.model.user.chat.UserChatRequestDto;
import com.example.webflux.model.user.chat.UserChatResponseDto;
import com.example.webflux.service.user.chat.UserChatService;
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

    private final UserChatService userChatService;

    /**
     * 단일 응답
     */
    @PostMapping("/oneshot")
    public Mono<UserChatResponseDto> oneShotChat(@RequestBody UserChatRequestDto userChatRequestDto){
        //서비스에서 request 가공해서 response 돌려줘야 함
        return userChatService.getOneShotChat(userChatRequestDto);
    }
}
