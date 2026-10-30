package com.fintrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fintrack.domain.ExpenseCategory;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, UUID> {

    @Query("SELECT c FROM ExpenseCategory c WHERE c.merchants IS EMPTY")
    List<ExpenseCategory> findUnusedExpenseCategories();
}
