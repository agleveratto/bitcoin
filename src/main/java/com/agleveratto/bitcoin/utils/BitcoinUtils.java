package com.example.winance.utils;

import com.example.winance.entities.Bitcoin;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.math.MathFlux;

@Slf4j
public class BitcoinUtils {

    /**
     * Get average value given a Flux<Bitcoin>
     *
     * @param bitcoinFlux Flux to analyze
     * @return averageValue
     */
    public Double getAverageValue(Flux<Bitcoin> bitcoinFlux){
        Mono<Double> averageValueMono = MathFlux.averageDouble(getPublisherByFlux(bitcoinFlux));
        Double[] averagePrice = {0.0};
        averageValueMono.subscribe(value -> averagePrice[0] =+Double.parseDouble(String.format("%.02f",value)));
        log.info("average price -> " + averagePrice[0]);
        return averagePrice[0];
    }

    /**
     * Get max value given a Flux<Bitcoin>
     *
     * @param bitcoinFlux Flux to analyze
     * @return maxValue
     */
    public Double getMaxValue(Flux<Bitcoin> bitcoinFlux){
        Mono<Double> maxValueMono = MathFlux.max(getPublisherByFlux(bitcoinFlux));
        Double[] maxPrice = {0.0};
        maxValueMono.subscribe(value -> maxPrice[0] =+ Double.parseDouble(String.format("%.02f",value)));
        log.info("max price -> " + maxPrice[0]);
        return maxPrice[0];

    }

    /**
     * Get percentage differential value between params
     *
     * To resolve this, apply this rule:
     * averageValue div 100 multiply maxValue
     *
     * In case that this is called when I have an only one row on BD and averageValue and maxValue returns same value and
     * the percentage differential value equals 100 and this is wrong, so for the math operation above apply a reduced 100
     * to get a real differential but as this value is a negative number, apply as last operation a multiply to -1
     *
     * @param averageValue of Flux <Bitcoin>
     * @param maxValue of Flux <Bitcoin>
     * @return percentage differential value
     */
    public Double getDifferentialValue(Double averageValue, Double maxValue){
        return ((averageValue * 100 / maxValue) - 100) * -1;
    }

    /**
     * Get Publisher Double given a Flux<Bitcoin>
     *
     * @param bitcoinFlux to get Publisher
     * @return publisher Double
     */
    protected Publisher<Double> getPublisherByFlux(Flux<Bitcoin> bitcoinFlux){
        return bitcoinFlux.flatMap(bitcoin -> Mono.just(bitcoin.getLprice()));
    }
}
