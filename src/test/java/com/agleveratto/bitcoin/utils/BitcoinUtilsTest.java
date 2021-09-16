package com.agleveratto.bitcoin.utils;

import com.agleveratto.bitcoin.entities.Bitcoin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BitcoinUtilsTest {

    BitcoinUtils bitcoinUtils = new BitcoinUtils();

    static Bitcoin bitcoin, bitcoin2;

    @BeforeAll
    static void setup(){
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        bitcoin = Bitcoin.builder().lprice(48165.0).curr1("BTC").curr2("USD").createdAt(now).build();
        bitcoin2 = Bitcoin.builder().lprice(48165.5).curr1("BTC").curr2("USD").createdAt(now).build();
    }

    @Test
    void givenFluxBitcoin_thenReturnAverageValue(){
        List<Bitcoin> bitcoinList = new ArrayList<>();
        bitcoinList.add(bitcoin);
        bitcoinList.add(bitcoin2);

        Double expected = (bitcoin.getLprice() + bitcoin2.getLprice()) / 2;
        Double response = bitcoinUtils.getAverageValue(bitcoinList);
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void givenFluxBitcoin_thenReturnMaxValue(){
        List<Bitcoin> bitcoinList = new ArrayList<>();
        bitcoinList.add(bitcoin);
        bitcoinList.add(bitcoin2);
        Double response = bitcoinUtils.getMaxValue(bitcoinList);
        assertThat(response).isEqualTo(bitcoin2.getLprice());
    }

    @Test
    void givenAverageValueAndMaxValue_thenReturnPercentageDifferantialValue(){
        List<Bitcoin> bitcoinList = new ArrayList<>();
        bitcoinList.add(bitcoin);
        bitcoinList.add(bitcoin2);
        Double averageValue = bitcoinUtils.getAverageValue(bitcoinList);
        Double maxValue = bitcoinUtils.getMaxValue(bitcoinList);
        Double expected = ((averageValue * 100 / maxValue) - 100) * -1;
        Double response = bitcoinUtils.getDifferentialValue(averageValue, maxValue);
        assertThat(response).isEqualTo(expected);
    }

}
