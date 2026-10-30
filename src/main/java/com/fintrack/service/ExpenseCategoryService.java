package com.fintrack.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintrack.domain.ExpenseCategory;
import com.fintrack.repository.ExpenseCategoryRepository;
import com.fintrack.repository.MerchantRepository;

@Service
public class ExpenseCategoryService {

    public static final String NOT_FOUND = "The expense category with that id does not exist";
    public static final String IN_USE = "That expense category is held by a merchant";

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepo;

    @Autowired
    private MerchantRepository merchantRepo;

    public List<ExpenseCategory> getAllExpenseCategories() {
        return expenseCategoryRepo.findAll();
    }

    public List<ExpenseCategory> getUnusedExpenseCategories() {
        return expenseCategoryRepo.findUnusedExpenseCategories();
    }

    public Optional<ExpenseCategory> getExpenseCategory(UUID id) {
        return expenseCategoryRepo.findById(id);
    }

    public String saveExpenseCategory(ExpenseCategory expenseCategory) {
        expenseCategoryRepo.save(expenseCategory);
        return "Expense category saved successfully";
    }

    public String updateExpenseCategory(UUID id, ExpenseCategory newData) {
        Optional<ExpenseCategory> found = expenseCategoryRepo.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }
        ExpenseCategory expenseCategory = found.get();
        expenseCategory.setName(newData.getName());
        expenseCategoryRepo.save(expenseCategory);
        return "Expense category updated successfully";
    }

    public String deleteExpenseCategory(UUID id) {
        if (!expenseCategoryRepo.existsById(id)) {
            return NOT_FOUND;
        }
        if (merchantRepo.existsByExpenseCategoriesId(id)) {
            return IN_USE;
        }
        expenseCategoryRepo.deleteById(id);
        return "Expense category deleted successfully";
    }
}
