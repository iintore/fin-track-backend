package com.fintrack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "merchant")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "trade_name")
    private String tradeName;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "stall_id")
    private Stall stall;

    @ManyToMany
    @JoinTable(
            name = "merchant_expense_category",
            joinColumns = @JoinColumn(name = "merchant_id"),
            inverseJoinColumns = @JoinColumn(name = "expense_category_id")
    )
    private List<ExpenseCategory> expenseCategories = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Stall getStall() {
        return stall;
    }

    public void setStall(Stall stall) {
        this.stall = stall;
    }

    public List<ExpenseCategory> getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }
}
