package com.fintrack.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintrack.domain.ExpenseStatus;
import com.fintrack.repository.AccountHolderRepository;
import com.fintrack.repository.ExpenseRepository;
import com.fintrack.repository.MerchantRepository;
import com.fintrack.repository.ReceiptLineRepository;
import com.fintrack.repository.StallRepository;

@Service
public class DashboardService {

    @Autowired
    private AccountHolderRepository accountHolderRepo;

    @Autowired
    private MerchantRepository merchantRepo;

    @Autowired
    private StallRepository stallRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private ReceiptLineRepository receiptLineRepo;

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("accountHolders", accountHolderRepo.count());
        summary.put("merchants", merchantRepo.count());
        summary.put("freeStalls", (long) stallRepo.findFreeStalls().size());
        summary.put("expensesToday", expenseRepo.countBySpendDate(LocalDate.now()));
        summary.put("planned", expenseRepo.countByStatus(ExpenseStatus.PLANNED));
        summary.put("receiptLines", receiptLineRepo.count());
        return summary;
    }
}
