package com.example.webflux.model.llmclient.gpt.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * GptResponseFormat
 *
 * @author yonse
 * @date 2025-10-11
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GptResponseFormat implements Serializable {
    @Serial
    private static final long serialVersionUID = 5329010667094056917L;

    private String type;

}