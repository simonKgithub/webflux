package com.example.webflux.model.llmclient.gemini.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * GeminiPart
 *
 * @author yonse
 * @date 2025-10-11
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeminiPart implements Serializable {
    @Serial
    private static final long serialVersionUID = -182691336286682081L;

    private String text;
}