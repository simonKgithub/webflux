package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class FluxMonoErrorAndSignalTest {

    /**
     * Flux.Mono.error()
     */

    @Test
    public void testBasicSignal(){
        Flux.just(1, 2, 3, 4)
                .doOnNext(publishedData -> System.out.println("publishedData = " + publishedData))
                .doOnComplete(() -> System.out.println("Stream is over"))
                .doOnError(ex -> {
                    System.out.println("ex error occurred !!! ");
                })
                .subscribe(data -> System.out.println("data = " + data));
    }

    @Test
    public void testFluxMonoError(){
        try {
            Flux.just(1, 2, 3, 4)
                    .map(data -> {
                        try {
                            if (data == 3) {
                                throw new RuntimeException();
                            }
                            return data * 2;
                        } catch (Exception e) {
//                            throw new RuntimeException();
//                            return data * 999;
                            throw new IllegalArgumentException();
                        }
                    })
                    .onErrorMap( ex -> new IllegalArgumentException() ) //에러 커스텀 가능
                    .onErrorReturn(999)
                    .onErrorComplete()
//                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe(data -> System.out.println("data = " + data));
        } catch (Exception e) {
            System.out.println("Error occurred!!!");
        }
    }

    //Flux.Mono.error()
    @Test
    public void testFluxMonoDotError(){
        Flux.just(1, 2, 3, 4)
                .flatMap(data -> {
                    if (data != 3) {
                        return Mono.just(data);
                    } else {
                        return Mono.error(new RuntimeException());
//                        throw new RuntimeException();
                    }
                }).subscribe(data-> System.out.println("data = " + data));
    }
}
