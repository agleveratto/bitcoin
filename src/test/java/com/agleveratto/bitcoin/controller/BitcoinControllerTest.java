package com.agleveratto.bitcoin.controller;

import com.agleveratto.bitcoin.service.BitcoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BitcoinController.class)
public class BitcoinControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BitcoinService bitcoinService;
}
