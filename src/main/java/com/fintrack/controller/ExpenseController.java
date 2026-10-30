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

import com.fintrack.domain.Expense;
import com.fintrack.service.ExpenseService;

@RestController
@RequestMapping(value = "/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveExpense(@RequestBody Expense expense) {

        String message = expenseService.saveExpense(expense);

        if (message.equals(ExpenseService.ACCOUNT_HOLDER_NOT_FOUND) || message.equals(ExpenseService.MERCHANT_NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.equals(ExpenseService.REFERENCES_REQUIRED)) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Expense>> getAllExpenses() {
        return new ResponseEntity<>(expenseService.getAllExpenses(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getExpense(@PathVariable UUID id) {

        Optional<Expense> expense = expenseService.getExpense(id);

        if (expense.isEmpty()) {
            return new ResponseEntity<>(ExpenseService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(expense.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateExpense(@PathVariable UUID id, @RequestBody Expense expense) {

        String message = expenseService.updateExpense(id, expense);

        if (message.equals(ExpenseService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals(ExpenseService.ACCOUNT_HOLDER_NOT_FOUND) || message.equals(ExpenseService.MERCHANT_NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.equals(ExpenseService.REFERENCES_REQUIRED)) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable UUID id) {

        String message = expenseService.deleteExpense(id);

        if (message.equals(ExpenseService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
