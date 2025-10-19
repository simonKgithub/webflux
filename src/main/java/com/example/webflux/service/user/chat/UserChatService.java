package com.example.webflux.service.user.chat;

import com.example.webflux.model.user.chat.UserChatRequestDto;
import com.example.webflux.model.user.chat.UserChatResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * UserChatService
 *
 * @author yonse
 * @date 2025-10-11
 */
public interface UserChatService {
    Mono<UserChatResponseDto> getOneShotChat(UserChatRequestDto userChatRequestDto);

    Flux<UserChatResponseDto> getOneShotChatStream(UserChatRequestDto userChatRequestDto);
}