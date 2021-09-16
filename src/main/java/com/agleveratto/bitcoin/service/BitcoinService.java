package com.agleveratto.bitcoin.service;

import com.agleveratto.bitcoin.entities.Bitcoin;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service to manipulate data from DB
 */
public interface BitcoinService {

    /**
     * Retrieve bitcoin in certain date
     *
     * @param createdAt date to find
     * @return a bitcoin
     * @throws ResourceNotFoundException if not result on DB
     */
    Bitcoin retrieveBitcoinByDate(LocalDateTime createdAt) throws ResourceNotFoundException;

    /**
     * find List of Bitcoin given two dates
     * @param sinceDate since createdAt value
     * @param untilDate until createdAt value
     * @return a List Bitcoin if exists
     * @throws ResourceNotFoundException if not result on DB
     */
    List<Bitcoin> retrieveListBitcoinFromDates(LocalDateTime sinceDate, LocalDateTime untilDate) throws ResourceNotFoundException;

}
