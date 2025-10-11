package com.example.webflux.model.llmclient;

import com.example.webflux.model.llmclient.gemini.response.GeminiChatResponseDto;
import com.example.webflux.model.llmclient.gpt.response.GptChatResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

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
public class LlmChatResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 7298249976663439340L;

    private String llmResponse;

    public LlmChatResponseDto(GptChatResponseDto responseDto) {
        this.llmResponse = responseDto.getSingleChoice().getMessage().getContent();
    }

    public LlmChatResponseDto(GeminiChatResponseDto responseDto) {
        this.llmResponse = responseDto.getSingleText();
    }
}