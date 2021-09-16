package com.agleveratto.bitcoin.controller;

import com.agleveratto.bitcoin.entities.Bitcoin;
import com.agleveratto.bitcoin.response.Response;
import com.agleveratto.bitcoin.service.impl.BitcoinServiceImpl;
import com.agleveratto.bitcoin.utils.BitcoinUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * Endpoint to retrieve average and percentage differential between the average and max value given Flux Bitcoin
     * created between since date and until date
     *
     * @param sinceDate since createdAt value
     * @param untilDate until createdAt value
     * @return a custom Response with required values (average value and percentage differential)
     */
    @GetMapping(value = "/priceBetweenDates")
    public Response retrievePriceBetweenDates(@RequestParam("sinceDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                      LocalDateTime sinceDate,
                                              @RequestParam("untilDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                      LocalDateTime untilDate) {
        try{
            /* Find Bitcoins prices between two dates */
            List<Bitcoin> bitcoinList = bitcoinService.retrieveListBitcoinFromDates(sinceDate, untilDate);
            /* Use Bitcoin utils */
            BitcoinUtils bitcoinUtils = new BitcoinUtils();
            /* Obtain average value */
            Double averagePrice = bitcoinUtils.getAverageValue(bitcoinList);
            /* Obtain max value */
            Double maxPrice = bitcoinUtils.getMaxValue(bitcoinList);
            /* Obtain porcentual differencial value */
            Double differentialValue = bitcoinUtils.getDifferentialValue(averagePrice, maxPrice);

            return new Response(averagePrice, String.format("%.2f", differentialValue).concat("%"));
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException(rnfe.getMessage());
        }
    }
}
