package com.nubiform.payment.repository;

import com.nubiform.payment.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
}
