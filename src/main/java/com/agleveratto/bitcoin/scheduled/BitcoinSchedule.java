package com.agleveratto.bitcoin.scheduled;

import com.agleveratto.bitcoin.entities.Bitcoin;
import com.agleveratto.bitcoin.repository.BitcoinRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Schedule to obtain a Bitcoin price
 */
@Component
@Slf4j
public class BitcoinSchedule {

    /* repository object */
    BitcoinRepository bitcoinRepository;

    /* gson to format rest template response to entity */
    Gson gson = new Gson();
    /* rest template to access to uri and retrieve response */
    RestTemplate restTemplate = new RestTemplate();

    /**
     * Constructor to inject object
     * @param bitcoinRepository to autowired
     */
    @Autowired
    public BitcoinSchedule(BitcoinRepository bitcoinRepository) {
        this.bitcoinRepository = bitcoinRepository;
    }

    /**
     * Task to get a bitcoin price every 10 secs
     */
    @Scheduled(fixedRate = 10000)
    public void getBitcoinPrice(){
        /* create a http headers to have access to call the uri */
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        /* URL */
        String URI = "https://cex.io/api/last_price/BTC/USD";

        /* call external api to obtain a bitcoin object */
        String bitcoinJson = restTemplate.exchange(URI, HttpMethod.GET, entity, String.class).getBody();

        /* get actual time and truncate to secs */
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        /* mapper String response to Bitcoin entity */
        Bitcoin bitcoin = gson.fromJson(bitcoinJson, Bitcoin.class);
        /* set actual time to bitcoin */
        bitcoin.setCreatedAt(now);

        /*save bitcoin to database*/
        bitcoinRepository.save(bitcoin);
    }
}
