package com.nubiform.payment.repository;

import com.nubiform.payment.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
//    List<History> findByOriginId(Long originId);
}
