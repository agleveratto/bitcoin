package com.agleveratto.bitcoin.service;

import com.agleveratto.bitcoin.entities.Bitcoin;
import com.agleveratto.bitcoin.repository.BitcoinRepository;
import com.agleveratto.bitcoin.service.impl.BitcoinServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BitcoinServiceTest {

    @InjectMocks
    BitcoinServiceImpl bitcoinService;

    @Mock
    BitcoinRepository bitcoinRepository;

    static Bitcoin bitcoin, bitcoin2;
    static List<Bitcoin> bitcoinList;

    static LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    static LocalDateTime sinceDate = LocalDateTime.MIN.truncatedTo(ChronoUnit.SECONDS);
    static LocalDateTime untilDate = LocalDateTime.MAX.truncatedTo(ChronoUnit.SECONDS);

    @BeforeAll
    static void setup(){
        bitcoin = Bitcoin.builder().lprice(48048.4).curr1("BTC").curr2("USD").createdAt(now).build();
        bitcoin2 = Bitcoin.builder().lprice(48049.2).curr1("BTC").curr2("USD").createdAt(now.plusHours(2)).build();

        bitcoinList = new ArrayList<>();
        bitcoinList.add(bitcoin);
        bitcoinList.add(bitcoin2);
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
        Executable executable = () -> bitcoinService.retrieveBitcoinByDate(LocalDateTime.MIN);
        assertThrows(ResourceNotFoundException.class, executable);
    }

    @Test
    void givenSinceDateAndUntilDate_thenReturnBitcoinList(){
        when(bitcoinRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(bitcoin.getCreatedAt().minusHours(1),
                bitcoin2.getCreatedAt().plusHours(1)))
                .thenReturn(bitcoinList);
        List<Bitcoin> response = bitcoinService.retrieveListBitcoinFromDates(bitcoin.getCreatedAt().minusHours(1),
                bitcoin2.getCreatedAt().plusHours(1));
        assertThat(response).isNotNull().isNotEmpty().contains(bitcoin).contains(bitcoin2);
    }

    @Test
    void givenSinceDateAndUntilDate_whenResultIsEmpty_thenReturnNull(){
        when(bitcoinRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(sinceDate, untilDate))
                .thenReturn(new ArrayList<>());
        Executable executable = () -> bitcoinService.retrieveListBitcoinFromDates(sinceDate, untilDate);
        assertThrows(ResourceNotFoundException.class, executable);
    }

    @Test
    void givenSinceDateAndUntilDate_whenResultIsNull_thenReturnNull(){
        when(bitcoinRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(sinceDate, untilDate))
                .thenReturn(null);
        Executable executable = () -> bitcoinService.retrieveListBitcoinFromDates(sinceDate, untilDate);
        assertThrows(ResourceNotFoundException.class, executable);
    }
}
