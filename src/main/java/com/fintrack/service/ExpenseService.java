package com.fintrack.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintrack.domain.AccountHolder;
import com.fintrack.domain.Expense;
import com.fintrack.domain.ExpenseStatus;
import com.fintrack.domain.Merchant;
import com.fintrack.repository.AccountHolderRepository;
import com.fintrack.repository.ExpenseRepository;
import com.fintrack.repository.MerchantRepository;
import com.fintrack.repository.ReceiptLineRepository;

@Service
public class ExpenseService {

    public static final String NOT_FOUND = "The expense with that id does not exist";
    public static final String ACCOUNT_HOLDER_NOT_FOUND = "The account holder with that id does not exist";
    public static final String MERCHANT_NOT_FOUND = "The merchant with that id does not exist";
    public static final String REFERENCES_REQUIRED = "Account holder and merchant are required";
    public static final String DATE_REQUIRED = "Expense date is required";
    public static final String DATE_IN_PAST = "Expense date cannot be in the past";
    public static final String MERCHANT_BUSY = "Merchant already has an open expense on that date";
    public static final String STATUS_UPDATED = "Expense status updated successfully";
    public static final String HAS_RECEIPT_LINES = "That expense has receipt lines, delete them first";

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private AccountHolderRepository accountHolderRepo;

    @Autowired
    private MerchantRepository merchantRepo;

    @Autowired
    private ReceiptLineRepository receiptLineRepo;

    public List<Expense> getAllExpenses() {
        return expenseRepo.findAll();
    }

    public Optional<Expense> getExpense(UUID id) {
        return expenseRepo.findById(id);
    }

    public List<Expense> getExpensesByStatus(ExpenseStatus status) {
        return expenseRepo.findByStatusOrderBySpendDateAsc(status);
    }

    public List<Expense> getExpensesBetween(LocalDate start, LocalDate end) {
        return expenseRepo.findBySpendDateBetweenOrderBySpendDateAsc(start, end);
    }

    public String saveExpense(Expense expense) {
        String problem = attachAccountHolderAndMerchant(expense, expense);
        if (problem != null) {
            return problem;
        }
        if (expense.getSpendDate() == null) {
            return DATE_REQUIRED;
        }
        if (expense.getSpendDate().isBefore(LocalDate.now())) {
            return DATE_IN_PAST;
        }
        boolean merchantBusy = expenseRepo.existsByMerchantIdAndSpendDateAndStatusNot(
                expense.getMerchant().getId(), expense.getSpendDate(), ExpenseStatus.VOIDED);
        if (merchantBusy) {
            return MERCHANT_BUSY;
        }
        expense.setStatus(ExpenseStatus.PLANNED);
        expenseRepo.save(expense);
        return "Expense saved successfully";
    }

    public String updateExpense(UUID id, Expense newData) {
        Optional<Expense> found = expenseRepo.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }
        Expense expense = found.get();
        String problem = attachAccountHolderAndMerchant(newData, expense);
        if (problem != null) {
            return problem;
        }
        expense.setSpendDate(newData.getSpendDate());
        expense.setDescription(newData.getDescription());
        expense.setAmount(newData.getAmount());
        expenseRepo.save(expense);
        return "Expense updated successfully";
    }

    public String changeStatus(UUID id, ExpenseStatus newStatus) {
        Optional<Expense> found = expenseRepo.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }
        Expense expense = found.get();
        ExpenseStatus current = expense.getStatus();
        if (!canMove(current, newStatus)) {
            return "Cannot change status from " + current + " to " + newStatus;
        }
        expense.setStatus(newStatus);
        expenseRepo.save(expense);
        return STATUS_UPDATED;
    }

    private boolean canMove(ExpenseStatus from, ExpenseStatus to) {
        if (from == ExpenseStatus.PLANNED) {
            return to == ExpenseStatus.PENDING || to == ExpenseStatus.VOIDED;
        }
        if (from == ExpenseStatus.PENDING) {
            return to == ExpenseStatus.SETTLED || to == ExpenseStatus.VOIDED;
        }
        return false;
    }

    public String deleteExpense(UUID id) {
        Optional<Expense> expense = expenseRepo.findById(id);
        if (expense.isEmpty()) {
            return NOT_FOUND;
        }
        if (receiptLineRepo.existsByExpense(expense.get())) {
            return HAS_RECEIPT_LINES;
        }
        expenseRepo.delete(expense.get());
        return "Expense deleted successfully";
    }

    private String attachAccountHolderAndMerchant(Expense request, Expense target) {
        if (request.getAccountHolder() == null || request.getAccountHolder().getId() == null
                || request.getMerchant() == null || request.getMerchant().getId() == null) {
            return REFERENCES_REQUIRED;
        }
        Optional<AccountHolder> accountHolder = accountHolderRepo.findById(request.getAccountHolder().getId());
        if (accountHolder.isEmpty()) {
            return ACCOUNT_HOLDER_NOT_FOUND;
        }
        Optional<Merchant> merchant = merchantRepo.findById(request.getMerchant().getId());
        if (merchant.isEmpty()) {
            return MERCHANT_NOT_FOUND;
        }
        target.setAccountHolder(accountHolder.get());
        target.setMerchant(merchant.get());
        return null;
    }
}
