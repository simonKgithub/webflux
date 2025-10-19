package com.example.webflux.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 기존 서블렛 기반에서는 jakarta 나 javax 기반 서블릿 객체를 받아서 사용
     * 여기서는 exchange 를 받아와 필요한 정보를 얻을 수 있다.
     * : 서비스 컨트롤러에서 발생하는 모든 exception 들이 exception 형태가 아닌 errorException 형태로 반환된다.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleGeneralException(Exception ex, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        log.error("[GeneralException] Request URI: {}, Method: {}, Error: {}", request.getURI(), request.getMethod(), ex.getMessage());

        CommonError commonError = new CommonError("500", ex.getMessage());

        return Mono.just(new ErrorResponse(commonError));
    }

    @ExceptionHandler(ErrorTypeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleErrorTypeException(ErrorTypeException ex, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        log.error("[GeneralException] Request URI: {}, Method: {}, Error: {}", request.getURI(), request.getMethod(), ex.getMessage());

        CommonError commonError = new CommonError(ex.getErrorType().getCode(), ex.getMessage());

        return Mono.just(new ErrorResponse(commonError));
    }
}
