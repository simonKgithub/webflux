package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicFluxOperatorTest {
    /**
     * Flux
     * 1) 데이터 시작
     *  데이터로부터: just, empty, from~시리즈
     *  함수로부터:
     *      - defer: Flux 객체를 return
     *      - create: 동기적인 객체를 return -> next()
     */
    @Test
    public void testFluxFromData(){
        //1) just
        Flux.just(1, 2, 3, 4)
                .subscribe(data -> System.out.println("data = " + data));

        //2) from~시리즈
        List<Integer> basicList = List.of(1, 2, 3, 4);
        Flux.fromIterable(basicList)
                .subscribe(data -> System.out.println("data fromIterable = " + data));
    }

    /**
     * Flux
     *  - defer : defer 안에서 Flux 객체를 반환한다.
     *  - create : 동기적인 객체를 자유롭게 반환해줘야 한다.
     *      - sink:
     */
    @Test
    public void testFluxFromFunction(){
        Flux.defer(() -> {
            return Flux.just(1, 2, 3, 4);
        }).subscribe(data-> System.out.println("data from defer = " + data));

        Flux.create(sink -> {
            sink.next(1);
            sink.next(2);
            sink.next(3);
            sink.complete();//sink 사용할 때 마지막에 호출(데이터가 언제 닫히는지 구독자에게 알려줘야한다.)
        }).subscribe(data-> System.out.println("data from create sink = " + data));
    }

    /**
     * sink 는 언제 사용할까?
     * - 동기적인 객체를 쉽게 마이그레이션 할 때
     * - 복잡한 로직 안에서 Flux 의 방출 타이밍을 제어하고 싶을 때
     */
    @Test
    public void testSinkDetail(){
        Flux.<String>create(sink -> {
            AtomicInteger counter = new AtomicInteger(0);
            recursiveFunction(sink);
        })
                .contextWrite(Context.of("counter", new AtomicInteger(0)))
                .subscribe(data -> System.out.println("data from recursive = " + data));
    }

    /**
     * 재귀함수
     *  - AtomicInteger: 여러 스레드가 접근을 해도 안전하게 연산을 할 수 있도록 도와주는 클래스. 보통 WebFlux 에서 counter 가 필요할 때 사용한다.
     * @param sink
     */
    public void recursiveFunction(FluxSink<String> sink){
        AtomicInteger counter = sink.contextView().get("counter");
        if (counter.incrementAndGet() < 10) { //++i
            sink.next("sink count " + counter);
            recursiveFunction(sink);
        } else {
            sink.complete();
        }
    }

    // tomcat: ThreadLocal -> 매개변수로 전달되는 객체가 여러군데 사용될 때 ThreadLocal 에 넣고 꺼내 씀
    // netty: context -> 매개변수로 전달되는 객체가 여러군데 사용될 때 context 에 넣고 사용한다.


    /**
     * 데이터 가공
     * Mono 에서 Flux 변환: flatMapMany
     * Flux 에서 Mono 변환: collectList
     */
    @Test
    public void testFluxCollectList(){
        Mono<List<Integer>> listMono = Flux.<Integer>just(1, 2, 3, 4, 5) //시작: 첫번째는 빈 함수로부터, 두번째는 데이터로부터 시작할 수 있다.
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .collectList();

        listMono.subscribe(data -> System.out.println("collectList return list data: "+data));
    }
}
