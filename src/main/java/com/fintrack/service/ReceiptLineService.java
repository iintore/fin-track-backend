package com.fintrack.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintrack.domain.Expense;
import com.fintrack.domain.ExpenseStatus;
import com.fintrack.domain.ReceiptLine;
import com.fintrack.repository.ExpenseRepository;
import com.fintrack.repository.ReceiptLineRepository;

@Service
public class ReceiptLineService {

    public static final String EXPENSE_NOT_FOUND = "The expense with that id does not exist";
    public static final String NOT_SETTLED = "Receipt lines are only for SETTLED expenses";
    public static final String NOT_FOUND = "The receipt line with that id does not exist";

    @Autowired
    private ReceiptLineRepository receiptLineRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    public String saveReceiptLine(UUID expenseId, ReceiptLine receiptLine) {
        Optional<Expense> expense = expenseRepo.findById(expenseId);
        if (expense.isEmpty()) {
            return EXPENSE_NOT_FOUND;
        }
        if (expense.get().getStatus() != ExpenseStatus.SETTLED) {
            return NOT_SETTLED;
        }
        receiptLine.setRecordedDate(LocalDate.now());
        receiptLine.setExpense(expense.get());
        receiptLineRepo.save(receiptLine);
        return "Receipt line saved successfully";
    }

    public List<ReceiptLine> getReceiptLinesOfAccountHolder(String walletNumber) {
        return receiptLineRepo.findByAccountHolderWalletNumber(walletNumber);
    }

    public String deleteReceiptLine(UUID id) {
        if (!receiptLineRepo.existsById(id)) {
            return NOT_FOUND;
        }
        receiptLineRepo.deleteById(id);
        return "Receipt line deleted successfully";
    }
}
