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

import com.fintrack.domain.AccountHolder;
import com.fintrack.service.AccountHolderService;

@RestController
@RequestMapping(value = "/api/account-holders")
public class AccountHolderController {

    @Autowired
    private AccountHolderService accountHolderService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveAccountHolder(@RequestBody AccountHolder accountHolder) {

        String message = accountHolderService.saveAccountHolder(accountHolder);

        if (message.equals(AccountHolderService.WALLET_TAKEN)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountHolder>> getAllAccountHolders() {
        return new ResponseEntity<>(accountHolderService.getAllAccountHolders(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAccountHolder(@PathVariable UUID id) {

        Optional<AccountHolder> accountHolder = accountHolderService.getAccountHolder(id);

        if (accountHolder.isEmpty()) {
            return new ResponseEntity<>(AccountHolderService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountHolder.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateAccountHolder(@PathVariable UUID id, @RequestBody AccountHolder accountHolder) {

        String message = accountHolderService.updateAccountHolder(id, accountHolder);

        if (message.equals(AccountHolderService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals(AccountHolderService.WALLET_TAKEN)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteAccountHolder(@PathVariable UUID id) {

        String message = accountHolderService.deleteAccountHolder(id);

        if (message.equals(AccountHolderService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.equals(AccountHolderService.IN_USE)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
