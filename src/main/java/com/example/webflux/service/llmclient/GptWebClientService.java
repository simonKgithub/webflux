package com.example.webflux.service.llmclient;

import com.example.webflux.exception.CustomErrorType;
import com.example.webflux.exception.ErrorTypeException;
import com.example.webflux.model.llmclient.LlmChatRequestDto;
import com.example.webflux.model.llmclient.LlmChatResponseDto;
import com.example.webflux.model.llmclient.LlmType;
import com.example.webflux.model.llmclient.gpt.request.GptChatRequestDto;
import com.example.webflux.model.llmclient.gpt.response.GptChatResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * GptWebClientService
 *
 * @author yonse
 * @date 2025-10-11
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GptWebClientService implements LlmWebClientService{

    private final WebClient webClient;

    @Value("${llm.gpt.key}")
    private String gptApiKey;

    @Override
    public Mono<LlmChatResponseDto> getChatCompletion(LlmChatRequestDto requestDto) {
        GptChatRequestDto gptChatRequestDto = new GptChatRequestDto(requestDto);

        return webClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + gptApiKey)
                .bodyValue(gptChatRequestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (clientResponse -> {
                    return clientResponse.bodyToMono(String.class).flatMap(body->{
                        log.error("Error Response: {}", body);
                        return Mono.error(new ErrorTypeException("API 요청 실패: " + body, CustomErrorType.GPT_RESPONSE_ERROR));
                    });
                }))
                .bodyToMono(GptChatResponseDto.class)
                .map(LlmChatResponseDto::new)
                ;
    }

    @Override
    public LlmType getLlmType() {
        return LlmType.GPT;
    }

    @Override
    public Flux<LlmChatResponseDto> getChatCompletionStream(LlmChatRequestDto requestDto) {
        GptChatRequestDto gptChatRequestDto = new GptChatRequestDto(requestDto);
        gptChatRequestDto.setStream(true);

        return webClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + gptApiKey)
                .bodyValue(gptChatRequestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (clientResponse -> {
                    return clientResponse.bodyToMono(String.class).flatMap(body->{
                        log.error("Error Response: {}", body);
                        return Mono.error(new ErrorTypeException("API 요청 실패: " + body, CustomErrorType.GPT_RESPONSE_ERROR));
                    });
                }))
                .bodyToFlux(GptChatResponseDto.class)
                .takeWhile(response -> Optional.ofNullable(response.getSingleChoice().getFinish_reason()).isEmpty())
                .map(LlmChatResponseDto::getLlmChatResponseDtoFromStream)
                ;
    }
}