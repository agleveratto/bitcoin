package com.agleveratto.bitcoin.controller;

import com.agleveratto.bitcoin.entities.Bitcoin;
import com.agleveratto.bitcoin.service.BitcoinService;
import com.agleveratto.bitcoin.service.impl.BitcoinServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BitcoinController.class)
public class BitcoinControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BitcoinController bitcoinController;

    static Bitcoin bitcoin;
    static LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    static String BITCOIN_PRICE_BY_DATE = "/v1/api/bitcoin/priceByDate";

    @BeforeAll
    static void setup(){
        bitcoin = Bitcoin.builder().lprice(48048.4).curr1("BTC").curr2("USD").createdAt(now).build();
    }

    @Test
    void givenNowDate_thenReturnBitcoin() throws Exception {
        when(bitcoinController.retrievePriceByDate(now)).thenReturn(bitcoin.getLprice());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BITCOIN_PRICE_BY_DATE)
                .param("createdAt", now.toString())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        assertThat(result).isNotNull();
        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(bitcoin.getLprice().toString());
    }

    @Test
    void givenRandomDate_thenReturnError404() throws Exception {
        when(bitcoinController.retrievePriceByDate(LocalDateTime.MIN)).thenThrow(ResourceNotFoundException.class);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BITCOIN_PRICE_BY_DATE)
                .param("createdAt", LocalDateTime.MIN.toString())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
        assertThat(result).isNotNull();
        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

}
