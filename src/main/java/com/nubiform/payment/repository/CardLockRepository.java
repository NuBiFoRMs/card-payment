package com.nubiform.payment.repository;

import com.nubiform.payment.domain.CardLock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardLockRepository extends JpaRepository<CardLock, String> {
}
