package com.agleveratto.bitcoin.service.impl;

import com.agleveratto.bitcoin.entities.Bitcoin;
import com.agleveratto.bitcoin.repository.BitcoinRepository;
import com.agleveratto.bitcoin.service.BitcoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link BitcoinService}
 */
@Service
public class BitcoinServiceImpl implements BitcoinService {

    /* repository object */
    BitcoinRepository bitcoinRepository;

    /**
     * Constructor to inject object
     * @param bitcoinRepository to autowired
     */
    @Autowired
    public BitcoinServiceImpl(BitcoinRepository bitcoinRepository) {
        this.bitcoinRepository = bitcoinRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bitcoin retrieveBitcoinByDate(LocalDateTime createdAt) throws ResourceNotFoundException {
        /* call repository method to get a bitcoin by createdAt */
        Bitcoin bitcoin = bitcoinRepository.findByCreatedAt(createdAt);
        /* if bitcoin not found, throw exception */
        if (bitcoin == null) {
            throw new ResourceNotFoundException("Price bitcoin not found for date: " + createdAt);
        }
        return bitcoin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Bitcoin> retrieveListBitcoinFromDates(LocalDateTime sinceDate, LocalDateTime untilDate) throws ResourceNotFoundException {
        /* call repository method to get a bitcoin by createdAt */
        List<Bitcoin> bitcoinList = bitcoinRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(sinceDate, untilDate);
        /* if bitcoin not found, throw exception */
        if (bitcoinList == null || bitcoinList.isEmpty()) {
            throw new ResourceNotFoundException("Price bitcoin not found between: " + sinceDate + " and " + untilDate);
        }
        return bitcoinList;
    }
}
