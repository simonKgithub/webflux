package com.example.webflux.model.llmclient.gemini.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * GeminiCandidate
 *
 * @author yonse
 * @date 2025-10-11
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeminiCandidate implements Serializable {
    @Serial
    private static final long serialVersionUID = 3103427709131152167L;

    private GeminiContent content;
}