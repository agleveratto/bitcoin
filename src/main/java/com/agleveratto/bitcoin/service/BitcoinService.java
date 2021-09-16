package com.agleveratto.bitcoin.service;

import com.agleveratto.bitcoin.entities.Bitcoin;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDateTime;

/**
 * Service to manipulate data from DB
 */
public interface BitcoinService {

    /**
     * Retrieve bitcoin in certain date
     *
     * @param createdAt date to find
     * @return a bitcoin
     */
    Bitcoin retrieveBitcoinByDate(LocalDateTime createdAt) throws ResourceNotFoundException;
}
