package com.nubiform.payment.repository;

import com.nubiform.payment.domain.Balance;
import com.nubiform.payment.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    //    List<History> findByOriginId(Long originId);
    List<History> findByBalanceAndType(Balance balance, String type);
}
