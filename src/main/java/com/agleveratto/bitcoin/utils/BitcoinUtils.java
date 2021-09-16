package com.agleveratto.bitcoin.utils;

import com.agleveratto.bitcoin.entities.Bitcoin;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class BitcoinUtils {

    /**
     * Get average value given a List<Bitcoin>
     *
     * @param bitcoinList List to analyze
     * @return averageValue
     */
    public Double getAverageValue(List<Bitcoin> bitcoinList){
        final Double[] totalValue = {0.0};
        bitcoinList.forEach(bitcoin -> totalValue[0] =+ bitcoin.getLprice());
        Double averageValue = totalValue[0] / Double.parseDouble(String.valueOf(bitcoinList.size()));
        log.info("average price -> {}", averageValue);
        return averageValue;
    }

    /**
     * Get max value given a Flux<Bitcoin>
     *
     * @param bitcoinList Flux to analyze
     * @return maxValue
     */
    public Double getMaxValue(List<Bitcoin> bitcoinList){
        Double maxValue = bitcoinList.stream().max(Comparator.comparing(Bitcoin::getLprice)).get().getLprice();
        log.info("max price -> {}", maxValue);
        return maxValue;

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
     * @param averageValue of List <Bitcoin>
     * @param maxValue of List <Bitcoin>
     * @return percentage differential value
     */
    public Double getDifferentialValue(Double averageValue, Double maxValue){
        return ((averageValue * 100 / maxValue) - 100) * -1;
    }
}
