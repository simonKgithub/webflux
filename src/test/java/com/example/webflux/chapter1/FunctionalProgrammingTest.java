package com.example.webflux.chapter1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

@SpringBootTest
public class FunctionalProgrammingTest {

    @Test
    public void produceOneToNineFluxOperator(){
        Flux.fromIterable(IntStream.rangeClosed(1,9).boxed().toList())
                .map( d -> d*4) //operator 대부분이 stream과 유사하게 동작
                .filter(d -> d%4==0)
                .subscribe(System.out::println);
    }

    @Test
    public void produceOneToNineFlux(){
        Flux<Integer> intFlux = Flux.create(sink -> {
            for (int i = 1; i <= 9; i++) {
                sink.next(i);
            }
            sink.complete();
        });

        intFlux.subscribe(data -> System.out.println("WebFlux가 구독 중!! : " + data));
        System.out.println("Netty 이벤트 루프로 스레드 복귀 !! ");
    }

    @Test
    public void produceOneToNineStream(){
        IntStream.rangeClosed(1,9).boxed()
                .map( d -> d*4)
                .filter(d -> d%4==0)
                .forEach(System.out::println);
        //최종연산자필요: collect, foreach, min, max
    }

    @Test
    public void funcProduceOneToNine(){
        List<Integer> sink = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            sink.add(i);
        }

        //*2를 전부 해주고 싶다
        sink = map(sink, (data) -> data * 4);

        //4의 배수들만 남겨두고 싶다
        sink = filter(sink, (data) -> data % 4 == 0);

        forEach(sink, (data) -> System.out.println(data));
    }

    private void forEach(List<Integer> sink, Consumer<Integer> consumer) {
        for (int i = 0; i < sink.size(); i++) {
            consumer.accept(sink.get(i));
        }
    }

    private List<Integer> filter(List<Integer> sink, Function<Integer, Boolean> predicate) {
        List<Integer> newSink2 = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            if (predicate.apply(sink.get(i)))
                newSink2.add(sink.get(i));
        }
        sink = newSink2;
        return sink;
    }

    private List<Integer> map(List<Integer> sink, Function<Integer, Integer> mapper) {
        List<Integer> newSink1 = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            newSink1.add(mapper.apply(sink.get(i)));
        }
        sink = newSink1;
        return sink;
    }

    @Test
    public void produceOneToNine(){
        List<Integer> sink = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            sink.add(i);
        }

        //*2를 전부 해주고 싶다
        List<Integer> newSink1 = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            newSink1.add(sink.get(i)*2);
        }
        sink = newSink1;

        //4의 배수들만 남겨두고 싶다
        List<Integer> newSink2 = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            if (sink.get(i)%4 == 0)
            newSink2.add(sink.get(i));
        }
        sink = newSink2;


        for (int i = 0; i < sink.size(); i++) {
            System.out.println(sink.get(i));
        }
    }

}
