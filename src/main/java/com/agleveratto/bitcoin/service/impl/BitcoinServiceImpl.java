package com.agleveratto.bitcoin.service.impl;

import com.agleveratto.bitcoin.entities.Bitcoin;
import com.agleveratto.bitcoin.repository.BitcoinRepository;
import com.agleveratto.bitcoin.service.BitcoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public Bitcoin retrieveBitcoinByDate(LocalDateTime createdAt) {
        /* call repository method to get a bitcoin by createdAt */
        return bitcoinRepository.findByCreatedAt(createdAt);
    }
}
