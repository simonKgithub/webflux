package com.example.webflux.model.llmclient.gpt;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum GptMessageRole {
    SYSTEM, //시스템 프롬프트
    USER,   //유저 입력
    ASSISTANT;//AI 응답

    @JsonValue
    @Override
    public String toString(){
        return name().toLowerCase();
    }
}
