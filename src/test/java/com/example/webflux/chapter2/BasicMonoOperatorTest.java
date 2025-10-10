package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class BasicMonoOperatorTest {

    /**
     * Flux, Mono 의 시작
     * 1. 데이터 시작 operator :
     *      - just: 일반적인 경우
     *      - empty: 특이한 상황(Optional.empty())
     *      - fromCallable: 동기적인 객체를 Mono 로 반환하고 싶을 때
     *      - defer: 코드의 흐름을 Mono 안에서 관리하면서 Mono 를 반환하고 싶을 때
     * 2. 데이터 가공 operator
     *      - map, filter
     *      - flatMapMany: Mono 에서 데이터 방출의 개수가 많아져서 Flux 로 바꾸고 싶다.
     * 3. 데이터 방출: subscribe
     */

    //데이터의 시작: just, empty
    @Test
    public void startMonoFromData(){
        Mono.just(1).subscribe(data -> System.out.println("just data = " + data));

        //데이터가 없어서 흐름 자체가 시작되지 않는다.
        //사소한 에러가 발생 했을 때 로그를 남기고 empty 인 Mono 를 전파
        Mono.empty().subscribe(data -> System.out.println("empty data = " + data));
    }

    //데이터의 시작: 함수(fromCallable, defer)

    /**
     * fromCallable -> 동기적인 객체를 Mono 로 반환할 때 사용
     * defer -> Mono 를 반환하고 싶을 때 사용
     */
    @Test
    public void startMonoFromFunction(){
        Mono<String> monoFromCallable = Mono.fromCallable(() -> {
            //우리 로직을 실행하고 동기적인 객체 반환
            return callRestTemplate("hello!");
        }).subscribeOn(Schedulers.boundedElastic());
        /**
         * fromCallable: 기존 레거시 프로젝트를 임시 마이그레이션 할 때 기존 객체를 Mono 로 감싸기 위해서 자주 사용한다.
         * -restTemplate, JPA 사용 시에도 fromCallable 자주 사용 >> 블로킹이 발생하는 라이브러리를 Mono 로 스레드 분리하여 처리
         */

        /**
         * Mono 객체를 Mono 객체로 반환하고 있다..?
         * (1), (2)의 차이는 Mono.just 에 스레드가 언제 도달하냐의 차이.
         * (1)은 monoFromDefer.subscribe()를 해야 도달한다.
         * (2)는 이미 완성된 데이터가 있다.
         * + 데이터가 언제 만들어지냐의 차이
         */

        //(1)
        Mono<String> monoFromDefer = Mono.defer(() -> {
            return callWebClient("Hello!");
        });
        monoFromDefer.subscribe();

        //(2)
        Mono<String> monoFromJust = callWebClient("Hello!");
    }

    @Test
    public void testDeferNecessary(){
        //abc 를 만드는 로직도 Mono 의 흐름 안에서 관리하고 싶다.
        Mono<String> stringMono = Mono.defer(() -> {
            String a = "He";
            String b = "ll"; //blocking 발생!
            String c = "o";
            return callWebClient(a + b + c);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> callWebClient(String request) {
        return Mono.just(request + " callWebClient");
    }

    public String callRestTemplate(String request) {
        return request + " callRestTemplate 응답";
    }

    /**
     * 데이터 가공: map, filter
     */
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

    /**
     * Mono 에서 데이터 방출의 개수가 많아져서 Flux 로 바꾸고 싶다 -> flatMapMany
     */
    @Test
    public void monoToFlux(){
        Mono<Integer> one = Mono.just(1);
        Flux<Integer> integerFlux = one.flatMapMany(data -> {
            return Flux.just(data, data + 1, data + 2);
        });
        integerFlux.subscribe(data -> System.out.println("data = " + data));
    }
}
