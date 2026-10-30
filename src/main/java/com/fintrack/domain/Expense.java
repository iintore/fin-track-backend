package com.fintrack.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "spend_date")
    private LocalDate spendDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExpenseStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private double amount;

    @ManyToOne
    @JoinColumn(name = "account_holder_id")
    private AccountHolder accountHolder;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    public UUID getId() {
        return id;
    }

    public LocalDate getSpendDate() {
        return spendDate;
    }

    public void setSpendDate(LocalDate spendDate) {
        this.spendDate = spendDate;
    }

    public ExpenseStatus getStatus() {
        return status;
    }

    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public AccountHolder getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(AccountHolder accountHolder) {
        this.accountHolder = accountHolder;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
}
