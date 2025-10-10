package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class SchedulerTest {

    /**
     * 스케줄러를 할당할 수 있는 구간
     * 1. subscribe
     * 2. publish
     */

    @Test
    public void testBasicFluxMono(){
        Mono.<Integer>just(2)
                .map(data->{
                    System.out.println("map Thread name = " + Thread.currentThread().getName());
                    return data*2;
                })
                .publishOn(Schedulers.parallel())
                .filter(data->{
                    System.out.println("filter Thread name = " + Thread.currentThread().getName());
                    return data%4==0;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(data-> System.out.println("Mono's subscribed data = " + data));
    }

}
