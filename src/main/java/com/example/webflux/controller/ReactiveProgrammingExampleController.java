package com.example.webflux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reactive")
@Slf4j
public class ReactiveProgrammingExampleController {


    @GetMapping("/onenine/legacy2")
    public Mono<List<Integer>> produceOneToNineLegacy2(){
        return Mono.defer(() -> {
            List<Integer> sink = new ArrayList<>();
            for (int i = 1; i <= 9; i++) {
                try {Thread.sleep(500);} catch (InterruptedException e) {} //4.5초 소요
                sink.add(i);
            }
            return Mono.just(sink);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    //1~9까지 출력하는 api
    @GetMapping("/onenine/legacy")
    public Mono<List<Integer>> produceOneToNineLegacy(){
        return Mono.<List<Integer>>fromCallable(()->{
            List<Integer> sink = new ArrayList<>();
            for (int i = 1; i <= 9; i++) {
                try {Thread.sleep(500);} catch (InterruptedException e) {} //4.5초 소요
                sink.add(i);
            }
            return sink;
        }).subscribeOn(Schedulers.boundedElastic());
    }

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
        return Flux.<Integer>create(sink -> {
            for (int i = 1; i <= 9; i++) {
                try {
                    log.info("Current Processing Thread: {}", Thread.currentThread().getName());//reactor-http-nio-x 스레드는 이벤트 루프 스레드이기 때문에 얘가 블로킹되어서는 안된다.
                    Thread.sleep(500);
                } catch (InterruptedException e) {} //4.5초 소요
                sink.next(i);
            }
            sink.complete();
        })
                .subscribeOn(Schedulers.boundedElastic()) //(1)구독하는 시점에 스레드를 변경하기
                ;
    }
    //리액티브 스트림 구현제 Flux, Mono를 사용하여 발생하는 데이터를 바로바로(Stream) 리액티브하게 처리
    //비동기로 동작 - 논 블로킹하게 동작 해야한다.

    //리액티브 프로그래밍 필수 요소
    //1. 데이터가 준비 될 때 마다 바로바로 리액티브하게 처리 > 리액티브 스트림 구현체 Flux, Mono를 사용하여 발생하는 데이터 바로바로 처리
    //2. 로직을 짤 때는 반드시 논 블로킹하게 짜야한다. > 이를 위해 비동기 프로그래밍이 필요하다.

    //1. 우리 코드를 포함한 애플리케이션의 작업 흐름을 실행시켜주는 것이 스레드이다.
    //2. Netty는 적은 수의 스레드만을 사용하기 때문에 블로킹에 매우 취약하다.

    //1. 우리 프로세스(웹서버)에서 스레드를 원하는 만큼 생성하고 마음대로 이용할 수 있다. > 이 스레드는 OS가 알아서 스케쥴링 해주기 때문에 CPU 자원 분배를 신경 쓸 필요가 없다.
    //2. 스레드를 너무 많이 생성하면 컨텍스트 스위칭 비용 등이 발생하기 때문에 Netty는 물리적인 스레드와 같은 개수의 스레드만을 사용한다.

    //1. I/O 블로킹이란 작업을 처리하는 스레드가 외부 리소스의 처리를 기다려야해서 작업이 중지된 채로 기다리고 있는 상태

    //1. Flux에는 데이터가 들어 있는게 아니라, 함수 코드 덩어리가 들어있다. 그래서 구독을 해서 함수를 실행시키는 순간부터 데이터가 발행된다.
    //2. 스레드가 1개라면 Flux만으로는 어떻게해도 블로킹을 회피할 수 없다. 스케줄러로 추가 스레드를 할당하여 대신 작업시켜야 한다.
}
