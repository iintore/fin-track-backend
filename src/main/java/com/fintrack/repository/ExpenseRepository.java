package com.fintrack.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fintrack.domain.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    boolean existsByMerchantId(UUID merchantId);

    boolean existsByAccountHolderId(UUID accountHolderId);
}
