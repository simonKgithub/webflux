package com.example.webflux.chapter2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class OperatorFlatMapTest {
    /**
     * Mono<Mono<T>> --> Mono<T>
     * Flux<Mono<T>> --> Flux<T>
     *      --> 이 구조 안에 있는 Mono 는 flatMap, merge 로 벗겨낼 수 있다.
     *      --> flatMap, merge 는 순서를 보장하지 않는다.
     *      --> 순서 보장이 필요하다면, sequential 을 사용하자.
     *
     * Mono<Flux<T>> --> Flux<T>
     *      --> flatMapMany 로 벗길 수 있다. 순서가 보장된다.
     *
     * Flux<Flux<T>> --> collecList --> Flux<Mono<List<T>>> --> Flux<List<T>>
     */


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

    @Test
    public void testWebClientFlatMap(){
        Flux<String> flatMap = Flux.just(
                callWebClient("1 step: cohensive problem", 1500),
                callWebClient("2 step: solving problem step by step", 1000),
                callWebClient("3 step: final response", 500)
        )
                .flatMap(monoData -> {
                    return monoData;
                });

        flatMap.subscribe(data -> System.out.println("FlatMapped data = " + data));


        Flux<String> flatMapSequential = Flux.just(
                callWebClient("1 step: cohensive problem", 1500),
                callWebClient("2 step: solving problem step by step", 1000),
                callWebClient("3 step: final response", 500)
        )
                .flatMapSequential(monoData -> {
                    return monoData;
                });

        flatMapSequential.subscribe(data -> System.out.println("FlatMapped Sequential data = " + data));


        Flux<String> merge = Flux.merge(
                callWebClient("1 step: cohensive problem", 1500),
                callWebClient("2 step: solving problem step by step", 1000),
                callWebClient("3 step: final response", 500)
        );
//        .map(~~~): 여기서 추가로 가공하면 flatMap 과 비슷한 구조가 된다.

        merge.subscribe(data -> System.out.println("Merged data = " + data));


        Flux<String> mergeSequential = Flux.mergeSequential(
                callWebClient("1 step: cohensive problem", 1500),
                callWebClient("2 step: solving problem step by step", 1000),
                callWebClient("3 step: final response", 500)
        );
//        .map(~~~): 여기서 추가로 가공하면 flatMap 과 비슷한 구조가 된다.

        mergeSequential.subscribe(data -> System.out.println("Merge Sequential data = " + data));

//        Flux<String> flatMap2 = Flux.<Mono<String>>create(sink -> {
//            sink.next(callWebClient("1 step: cohensive problem", 1500));
//            sink.next(callWebClient("2 step: solving problem step by step", 1000));
//            sink.next(callWebClient("3 step: final response", 500));
//            sink.complete();
//        }).flatMap(monoData -> {
//            return monoData;
//        });

        //main thread 잡아놓기
        try{
            Thread.sleep(10000);
        }catch (Exception e){}
    }

    //concat, concatMap 이런건 쓰지 말자. 순서대로(비효율적으로) 동작하기 때문이다.

    //비동기 흐름을 창출하는코드
    public Mono<String> callWebClient(String request, long delay) {
        return Mono.defer(() -> {
            try{
                log.info("Current Thread: {}", Thread.currentThread().getName());
                Thread.sleep(delay);
                return Mono.just(request + " -> delay: " + delay);
            }catch (Exception e){}
                return Mono.empty();
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
