package com.example.webflux.model.llmclient;

import com.example.webflux.exception.CommonError;
import com.example.webflux.model.llmclient.gemini.response.GeminiChatResponseDto;
import com.example.webflux.model.llmclient.gpt.response.GptChatResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

/**
 * LlmChatResponseDto
 *
 * @author yonse
 * @date 2025-10-11
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class LlmChatResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 7298249976663439340L;

    private String title;
    private String llmResponse;
    private CommonError error;

    public boolean isValid(){
        return Optional.ofNullable(error).isEmpty();
    }

    public LlmChatResponseDto(String title, String llmResponse) {
        this.title = title;
        this.llmResponse = llmResponse;
    }

    public LlmChatResponseDto(String llmResponse) {
        this.llmResponse = llmResponse;
    }

    public LlmChatResponseDto(CommonError error) {
        log.error("[LlmResponseError] LlmResponseError: {}", error);
        this.error = error;
    }

    public LlmChatResponseDto(CommonError error, Throwable ex) {
        log.error("[LlmResponseError] LlmResponseError: {}", error, ex);
        this.error = error;
    }

    public LlmChatResponseDto(GptChatResponseDto responseDto) {
        this.llmResponse = responseDto.getSingleChoice().getMessage().getContent();
    }

    public static LlmChatResponseDto getLlmChatResponseDtoFromStream(GptChatResponseDto gptChatResponseDto) {
        return new LlmChatResponseDto(gptChatResponseDto.getSingleChoice().getDelta().getContent());
    }


    public LlmChatResponseDto(GeminiChatResponseDto responseDto) {
        this.llmResponse = responseDto.getSingleText();
    }
}