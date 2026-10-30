package com.fintrack.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fintrack.domain.Expense;
import com.fintrack.domain.ExpenseStatus;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    boolean existsByMerchantId(UUID merchantId);

    boolean existsByAccountHolderId(UUID accountHolderId);

    List<Expense> findByStatusOrderBySpendDateAsc(ExpenseStatus status);

    List<Expense> findBySpendDateBetweenOrderBySpendDateAsc(LocalDate start, LocalDate end);
}
