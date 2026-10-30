package com.fintrack.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintrack.domain.AccountHolder;
import com.fintrack.repository.AccountHolderRepository;
import com.fintrack.repository.ExpenseRepository;

@Service
public class AccountHolderService {

    public static final String NOT_FOUND = "The account holder with that id does not exist";
    public static final String WALLET_TAKEN = "An account holder with that walletNumber already exists";
    public static final String IN_USE = "That account holder has expenses";

    @Autowired
    private AccountHolderRepository accountHolderRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    public List<AccountHolder> getAllAccountHolders() {
        return accountHolderRepo.findAll();
    }

    public Optional<AccountHolder> getAccountHolder(UUID id) {
        return accountHolderRepo.findById(id);
    }

    public List<AccountHolder> getAccountHoldersByLastName(String lastName) {
        return accountHolderRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public String saveAccountHolder(AccountHolder accountHolder) {
        if (walletTaken(accountHolder.getWalletNumber(), null)) {
            return WALLET_TAKEN;
        }
        accountHolderRepo.save(accountHolder);
        return "Account holder saved successfully";
    }

    public String updateAccountHolder(UUID id, AccountHolder newData) {
        Optional<AccountHolder> found = accountHolderRepo.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }
        if (walletTaken(newData.getWalletNumber(), id)) {
            return WALLET_TAKEN;
        }
        AccountHolder accountHolder = found.get();
        accountHolder.setFirstName(newData.getFirstName());
        accountHolder.setLastName(newData.getLastName());
        accountHolder.setWalletNumber(newData.getWalletNumber());
        accountHolder.setEmail(newData.getEmail());
        accountHolderRepo.save(accountHolder);
        return "Account holder updated successfully";
    }

    public String deleteAccountHolder(UUID id) {
        if (!accountHolderRepo.existsById(id)) {
            return NOT_FOUND;
        }
        if (expenseRepo.existsByAccountHolderId(id)) {
            return IN_USE;
        }
        accountHolderRepo.deleteById(id);
        return "Account holder deleted successfully";
    }

    private boolean walletTaken(String walletNumber, UUID id) {
        if (walletNumber == null) {
            return false;
        }
        return accountHolderRepo.findByWalletNumber(walletNumber)
                .filter(other -> !other.getId().equals(id))
                .isPresent();
    }
}
