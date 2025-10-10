package com.example.webflux.chapter1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class WebClientTest {

    private WebClient webClient = WebClient.builder().build();

    @Test
    public void testWebClient(){
        /**
         * webClient 사용 시 blocking 이벤트는 os에 위임한다.
         * webClient를 사용하면 스케줄러의 스레드 지원 없이도 완벽하게 비동기로 블로킹 회피가 가능해진다.
         * - 이걸 사용하면 스케줄러를 따로 할당해야 하는 문제도 해결된다.
         */
        Flux<Integer> intFlux = webClient.get()
                .uri("http://localhost:8080/reactive/onenine/flux")
                .retrieve()
                .bodyToFlux(Integer.class);


        intFlux.subscribe(data -> {
            System.out.println("Processing Thread name : " + Thread.currentThread().getName());
            System.out.println("WebFlux is subscribing now !! : " + data);
        });
        System.out.println("This Thread back to Netty's Event-Loop !! ");//블로킹 코드가 들어있는 Flux 코드는 스케쥴러의 스레드가 구독하고 실행시킨다. 메인 스레드는 이 코드를 그냥 통과한다.
        System.out.println("Main Thread name : " + Thread.currentThread().getName());

        try{
            Thread.sleep(7000); //테스트 코드에서는 메인스레드를 잡아놔야지 다른 스레드들도 동작한다. 임의로 메인 스레드를 살리기 위해 붙잡아둔다.
        } catch (Exception e) {}
    }

}
