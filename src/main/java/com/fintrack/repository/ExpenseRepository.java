package com.fintrack.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fintrack.domain.Expense;
import com.fintrack.domain.ExpenseStatus;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    boolean existsByMerchantId(UUID merchantId);

    boolean existsByAccountHolderId(UUID accountHolderId);

    List<Expense> findByStatusOrderBySpendDateAsc(ExpenseStatus status);

    List<Expense> findBySpendDateBetweenOrderBySpendDateAsc(LocalDate start, LocalDate end);

    boolean existsByMerchantIdAndSpendDateAndStatusNot(UUID merchantId, LocalDate spendDate, ExpenseStatus status);

    long countBySpendDate(LocalDate spendDate);

    long countByStatus(ExpenseStatus status);

    @Query("SELECT e.status, COUNT(e) FROM Expense e GROUP BY e.status")
    List<Object[]> countExpensesPerStatus();

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Expense e
            SET e.status = com.fintrack.domain.ExpenseStatus.VOIDED
            WHERE e.merchant.id = :merchantId
              AND e.spendDate = :date
              AND e.status <> com.fintrack.domain.ExpenseStatus.SETTLED
            """)
    int cancelDayOfMerchant(@Param("merchantId") UUID merchantId, @Param("date") LocalDate date);
}
