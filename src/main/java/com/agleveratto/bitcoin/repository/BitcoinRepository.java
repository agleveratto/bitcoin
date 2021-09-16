package com.agleveratto.bitcoin.repository;

import com.agleveratto.bitcoin.entities.Bitcoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BitcoinRepository extends JpaRepository<Bitcoin, Long> {
}
