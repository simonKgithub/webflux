package com.example.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class SubscribePublisherAsyncTest {

    @Test
    public void produceOneToNineFlux(){
        /**
         * 블로킹 회피 : 메인 스레드를 붙잡아두지 않고, 다른 스레드를 할당하는 것
         */
        Flux<Integer> intFlux = Flux.<Integer>create(sink -> {
            for (int i = 1; i <= 9; i++) {

                try{
                    Thread.sleep(500); //블로킹 코드
                } catch (Exception e) {}

                sink.next(i);
            }
            sink.complete();
        }).subscribeOn(Schedulers.boundedElastic());//블로킹 코드가 들어있는 Flux 코드는 스케쥴러의 스레드가 구독하고 실행시킨다. 메인 스레드는 이 코드를 그냥 통과한다.

        intFlux.subscribe(data -> {
            System.out.println("Processing Thread name : " + Thread.currentThread().getName());
            System.out.println("WebFlux is subscribing now !! : " + data);
        });
        System.out.println("This Thread back to Netty's Event-Loop !! ");//블로킹 코드가 들어있는 Flux 코드는 스케쥴러의 스레드가 구독하고 실행시킨다. 메인 스레드는 이 코드를 그냥 통과한다.

        try{
            Thread.sleep(7000); //테스트 코드에서는 메인스레드를 잡아놔야지 다른 스레드들도 동작한다. 임의로 메인 스레드를 살리기 위해 붙잡아둔다.
        } catch (Exception e) {}

    }
}
