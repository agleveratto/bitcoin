package com.agleveratto.bitcoin.service;

import com.agleveratto.bitcoin.entities.Bitcoin;
import com.agleveratto.bitcoin.repository.BitcoinRepository;
import com.agleveratto.bitcoin.service.impl.BitcoinServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BitcoinServiceTest {

    @InjectMocks
    BitcoinServiceImpl bitcoinService;

    @Mock
    BitcoinRepository bitcoinRepository;

    static Bitcoin bitcoin;
    static LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    @BeforeAll
    static void setup(){
        bitcoin = Bitcoin.builder().lprice(48048.4).curr1("BTC").curr2("USD").createdAt(now).build();
    }

    @Test
    void givenNowDate_thenReturnBitcoin(){
        when(bitcoinRepository.findByCreatedAt(now)).thenReturn(bitcoin);
        Bitcoin response = bitcoinService.retrieveBitcoinByDate(now);
        assertThat(response).isNotNull().isEqualTo(bitcoin);
    }

    @Test
    void givenRandomDate_thenReturnNull(){
        when(bitcoinRepository.findByCreatedAt(LocalDateTime.MIN)).thenReturn(null);
        Bitcoin response = bitcoinService.retrieveBitcoinByDate(LocalDateTime.MIN);
        assertThat(response).isNull();
    }
}
