package com.agleveratto.bitcoin.repository;

import com.agleveratto.bitcoin.entities.Bitcoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository for manipulate Bitcoin data from/to DB
 */
@Repository
public interface BitcoinRepository extends JpaRepository<Bitcoin, Long> {

    /**
     * Find Bitcoin by Created At
     * @param createdAt to find
     * @return a Bitcoin
     */
    Bitcoin findByCreatedAt(LocalDateTime createdAt);
}
