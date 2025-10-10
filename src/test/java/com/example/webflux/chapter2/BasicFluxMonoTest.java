package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BasicFluxMonoTest {

    @Test
    public void testFluxMonoBlock(){
        /**
         * Mono 는 언제 방출될 지 모르는 비동기 객체
         */
        Mono<String> justString = Mono.just("String");
        String string = justString.block();
        System.out.println("string = " + string);
    }

    @Test
    public void testBasicFluxMono(){
        Flux.<Integer>just(1, 2, 3, 4, 5) //시작: 첫번째는 빈 함수로부터, 두번째는 데이터로부터 시작할 수 있다.
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("Flux's subscribe data: "+data));

        //1. just 데이터로부터 흐름을 시작함
        //2. map 과 filter 같은 연산자로 데이터를 가공
        //3. subscribe 하면서 데이터를 방출함

        //Mono 0개부터 1개의 데이터마 방출할 수 있는 객체 -> Optional 정도
        //Flux 0개 이상의 데이터를 방출할 수 있는 객체 -> List, Stream 0개 이상의 데이터 방출

        Mono.<Integer>just(2) //시작: 첫번째는 빈 함수로부터, 두번째는 데이터로부터 시작할 수 있다.
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("Mono's subscribe data: "+data));
    }
}
