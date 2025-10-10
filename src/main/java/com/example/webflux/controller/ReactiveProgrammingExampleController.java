package com.example.webflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reactive")
public class ReactiveProgrammingExampleController {

    //1~9까지 출력하는 api
    @GetMapping("/onenine/list")
    public List<Integer> produceOneToNine(){
        List<Integer> sink = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            try {Thread.sleep(500);} catch (InterruptedException e) {} //4.5초 소요
            sink.add(i);
        }

        return sink;
    }

    @GetMapping("/onenine/flux")
    public Flux<Integer> produceOneToNineFlux(){
        return Flux.create(sink -> {
            for (int i = 1; i <= 9; i++) {
                try {Thread.sleep(500);} catch (InterruptedException e) {} //4.5초 소요
                sink.next(i);
            }
            sink.complete();
        });
    }
    //리액티브 스트림 구현제 Flux, Mono를 사용하여 발생하는 데이터를 바로바로(Stream) 리액티브하게 처리
    //비동기로 동작 - 논 블로킹하게 동작 해야한다.

    //리액티브 프로그래밍 필수 요소
    //1. 데이터가 준비 될 때 마다 바로바로 리액티브하게 처리 > 리액티브 스트림 구현체 Flux, Mono를 사용하여 발생하는 데이터 바로바로 처리
    //2. 로직을 짤 때는 반드시 논 블로킹하게 짜야한다. > 이를 위해 비동기 프로그래밍이 필요하다.
}
