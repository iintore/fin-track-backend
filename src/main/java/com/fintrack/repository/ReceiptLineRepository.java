package com.fintrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fintrack.domain.Expense;
import com.fintrack.domain.ReceiptLine;

@Repository
public interface ReceiptLineRepository extends JpaRepository<ReceiptLine, UUID> {

    @Query("SELECT d FROM ReceiptLine d WHERE d.expense.accountHolder.walletNumber = :walletNumber ORDER BY d.recordedDate DESC")
    List<ReceiptLine> findByAccountHolderWalletNumber(@Param("walletNumber") String walletNumber);

    boolean existsByExpense(Expense expense);
}
