package com.nubiform.payment.repository;

import com.nubiform.payment.domain.Sent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentRepository extends JpaRepository<Sent, Long> {
}
