package com.example.webflux.model.llmclient.gemini.request;

import com.example.webflux.model.llmclient.LlmChatRequestDto;
import com.example.webflux.model.llmclient.gemini.GeminiMessageRole;
import com.example.webflux.model.llmclient.gemini.response.GeminiContent;
import com.example.webflux.model.llmclient.gemini.response.GeminiPart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * GeminiChatRequestDto
 *
 * @author yonse
 * @date 2025-10-11
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GeminiChatRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5319891474638408930L;

    private List<GeminiContent> contents;
    private GeminiContent systemInstruction;
    private GeminiGenerationConfigDto generationConfig;

    public GeminiChatRequestDto(LlmChatRequestDto llmChatRequestDto) {
        if (llmChatRequestDto.isUseJson()) {
            this.generationConfig = new GeminiGenerationConfigDto();
        }
        this.contents = List.of(new GeminiContent(List.of(new GeminiPart(llmChatRequestDto.getUserRequest())), GeminiMessageRole.USER));
        this.systemInstruction = new GeminiContent(List.of(new GeminiPart(llmChatRequestDto.getSystemPrompt())));
    }
}