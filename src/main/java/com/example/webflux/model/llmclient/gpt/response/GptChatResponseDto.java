package com.example.webflux.model.llmclient.gpt.response;

import com.example.webflux.exception.CustomErrorType;
import com.example.webflux.exception.ErrorTypeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * GptChatResponseDto
 *
 * @author yonse
 * @date 2025-10-11
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GptChatResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7248605352938908880L;

    private List<GptChoice> choices;

    public GptChoice getSingleChoice(){
        return choices.stream().findFirst().orElseThrow(() -> {
            throw new ErrorTypeException("[GptResponse] There is no choices.", CustomErrorType.GPT_RESPONSE_ERROR);
        });
    }
}