package com.agleveratto.bitcoin.controller;

import com.agleveratto.bitcoin.entities.Bitcoin;
import com.agleveratto.bitcoin.response.Response;
import com.agleveratto.bitcoin.service.impl.BitcoinServiceImpl;
import com.agleveratto.bitcoin.utils.BitcoinUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BitcoinController.class)
public class BitcoinControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BitcoinController bitcoinController;

    @MockBean
    BitcoinServiceImpl bitcoinService;

    static Bitcoin bitcoin, bitcoin2;
    static List<Bitcoin> bitcoinList;

    BitcoinUtils bitcoinUtils = new BitcoinUtils();

    static LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    static LocalDateTime sinceDate = LocalDateTime.MIN.truncatedTo(ChronoUnit.SECONDS);
    static LocalDateTime untilDate = LocalDateTime.MAX.truncatedTo(ChronoUnit.SECONDS);

    static String BITCOIN_PATH = "/v1/api/bitcoin";
    static String BITCOIN_PRICE_BY_DATE = BITCOIN_PATH + "/priceByDate";
    static String BITCOIN_AVERAGE_PRICE_AND_PERCENTAGE_DIFFERENTIAL_BY_DATE = BITCOIN_PATH + "/priceBetweenDates";

    @BeforeAll
    static void setup(){
        bitcoin = Bitcoin.builder().lprice(48048.4).curr1("BTC").curr2("USD").createdAt(now).build();
        bitcoin2 = Bitcoin.builder().lprice(48050.4).curr1("BTC").curr2("USD").createdAt(now.plusHours(2)).build();

        bitcoinList = new ArrayList<>();
        bitcoinList.add(bitcoin);
        bitcoinList.add(bitcoin2);
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

    @Test
    void givenSinceDateAndUntilDate_thenReturnBitcoinList() throws Exception {
        when(bitcoinService.retrieveListBitcoinFromDates(bitcoin.getCreatedAt().minusHours(1), bitcoin2.getCreatedAt().plusHours(1)))
                .thenReturn(bitcoinList);
        Double averageValue = bitcoinUtils.getAverageValue(bitcoinList);
        Double maxValue = bitcoinUtils.getMaxValue(bitcoinList);
        Double differentialValue = bitcoinUtils.getDifferentialValue(averageValue, maxValue);
        Response expected = new Response(averageValue, String.format("%.2f", differentialValue).concat("%"));
        when(bitcoinController.retrievePriceBetweenDates(bitcoin.getCreatedAt().minusHours(1), bitcoin2.getCreatedAt().plusHours(1)))
                .thenReturn(expected);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BITCOIN_AVERAGE_PRICE_AND_PERCENTAGE_DIFFERENTIAL_BY_DATE)
                .param("sinceDate", bitcoin.getCreatedAt().minusHours(1).toString())
                .param("untilDate", bitcoin2.getCreatedAt().plusHours(1).toString())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        assertThat(result).isNotNull();
        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(new Gson().toJson(expected));
    }

    @Test
    void givenSinceDateAndUntilDate_whenResultIsEmpty_thenReturnNull() throws Exception {
        when(bitcoinService.retrieveListBitcoinFromDates(sinceDate, untilDate)).thenReturn(new ArrayList<>());
        when(bitcoinController.retrievePriceBetweenDates(sinceDate, untilDate)).thenThrow(ResourceNotFoundException.class);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BITCOIN_AVERAGE_PRICE_AND_PERCENTAGE_DIFFERENTIAL_BY_DATE)
                .param("sinceDate", sinceDate.toString())
                .param("untilDate", untilDate.toString())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
        assertThat(result).isNotNull();
        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void givenSinceDateAndUntilDate_whenResultIsNull_thenReturnNull() throws Exception {
        when(bitcoinService.retrieveListBitcoinFromDates(sinceDate, untilDate)).thenReturn(null);
        when(bitcoinController.retrievePriceBetweenDates(sinceDate, untilDate)).thenThrow(ResourceNotFoundException.class);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BITCOIN_AVERAGE_PRICE_AND_PERCENTAGE_DIFFERENTIAL_BY_DATE)
                .param("sinceDate", sinceDate.toString())
                .param("untilDate", untilDate.toString())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
        assertThat(result).isNotNull();
        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
