package com.fintrack.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintrack.domain.ExpenseCategory;
import com.fintrack.service.ExpenseCategoryService;

@RestController
@RequestMapping(value = "/api/expense-categories")
public class ExpenseCategoryController {

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveExpenseCategory(@RequestBody ExpenseCategory expenseCategory) {

        String message = expenseCategoryService.saveExpenseCategory(expenseCategory);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExpenseCategory>> getAllExpenseCategories() {
        return new ResponseEntity<>(expenseCategoryService.getAllExpenseCategories(), HttpStatus.OK);
    }

    @GetMapping(value = "/unused", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExpenseCategory>> getUnusedExpenseCategories() {
        return new ResponseEntity<>(expenseCategoryService.getUnusedExpenseCategories(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getExpenseCategory(@PathVariable UUID id) {

        Optional<ExpenseCategory> expenseCategory = expenseCategoryService.getExpenseCategory(id);

        if (expenseCategory.isEmpty()) {
            return new ResponseEntity<>(ExpenseCategoryService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(expenseCategory.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateExpenseCategory(@PathVariable UUID id, @RequestBody ExpenseCategory expenseCategory) {

        String message = expenseCategoryService.updateExpenseCategory(id, expenseCategory);

        if (message.equals(ExpenseCategoryService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteExpenseCategory(@PathVariable UUID id) {

        String message = expenseCategoryService.deleteExpenseCategory(id);

        if (message.equals(ExpenseCategoryService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.equals(ExpenseCategoryService.IN_USE)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
