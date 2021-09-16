package com.agleveratto.bitcoin.controller;

import com.agleveratto.bitcoin.service.impl.BitcoinServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;

/**
 * RestController Bitcoin Api
 */
@RestController
@RequestMapping(value = "v1/api/bitcoin")
public class BitcoinController {

    /* service object */
    BitcoinServiceImpl bitcoinService;

    /**
     * Constructor to inject object
     * @param bitcoinService to autowired
     */
    @Autowired
    public BitcoinController(BitcoinServiceImpl bitcoinService) {
        this.bitcoinService = bitcoinService;
    }

    /**
     * Endpoint to retrieve Price of Bitcoin by Date
     * @param createdAt to find
     * @return a bitcoin price
     */
    @GetMapping(value = "/priceByDate")
    Double retrievePriceByDate(@RequestParam("createdAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                   LocalDateTime createdAt){
        return bitcoinService.retrieveBitcoinByDate(createdAt).getLprice();
    }
}
