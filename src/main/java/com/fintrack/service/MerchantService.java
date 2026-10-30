package com.fintrack.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fintrack.domain.ExpenseCategory;
import com.fintrack.domain.Merchant;
import com.fintrack.domain.Stall;
import com.fintrack.repository.ExpenseCategoryRepository;
import com.fintrack.repository.ExpenseRepository;
import com.fintrack.repository.MerchantRepository;
import com.fintrack.repository.StallRepository;

@Service
public class MerchantService {

    public static final String NOT_FOUND = "The merchant with that id does not exist";
    public static final String STALL_NOT_FOUND = "The stall with that id does not exist";
    public static final String CATEGORY_NOT_FOUND = "The expense category with that id does not exist";
    public static final String IN_USE = "That merchant has expenses";

    @Autowired
    private MerchantRepository merchantRepo;

    @Autowired
    private StallRepository stallRepo;

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    public List<Merchant> getAllMerchants() {
        return merchantRepo.findAll();
    }

    public Optional<Merchant> getMerchant(UUID id) {
        return merchantRepo.findById(id);
    }

    public List<Merchant> getMerchantsByExpenseCategory(String name) {
        return merchantRepo.findMerchantsByExpenseCategoryName(name);
    }

    @Transactional
    public String saveMerchant(Merchant merchant) {
        String problem = attachStallAndCategories(merchant, merchant);
        if (problem != null) {
            return problem;
        }
        merchantRepo.save(merchant);
        return "Merchant saved successfully";
    }

    @Transactional
    public String updateMerchant(UUID id, Merchant newData) {
        Optional<Merchant> found = merchantRepo.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }
        Merchant merchant = found.get();
        String problem = attachStallAndCategories(newData, merchant);
        if (problem != null) {
            return problem;
        }
        merchant.setTradeName(newData.getTradeName());
        merchant.setOwnerName(newData.getOwnerName());
        merchant.setPhone(newData.getPhone());
        merchantRepo.save(merchant);
        return "Merchant updated successfully";
    }

    @Transactional
    public String deleteMerchant(UUID id) {
        if (!merchantRepo.existsById(id)) {
            return NOT_FOUND;
        }
        if (expenseRepo.existsByMerchantId(id)) {
            return IN_USE;
        }
        merchantRepo.deleteById(id);
        return "Merchant deleted successfully";
    }

    private String attachStallAndCategories(Merchant request, Merchant target) {
        Stall stall = null;
        if (request.getStall() != null) {
            UUID stallId = request.getStall().getId();
            Optional<Stall> foundStall = stallId == null ? Optional.empty() : stallRepo.findById(stallId);
            if (foundStall.isEmpty()) {
                return STALL_NOT_FOUND;
            }
            stall = foundStall.get();
        }
        List<ExpenseCategory> categories = new ArrayList<>();
        for (ExpenseCategory requested : request.getExpenseCategories()) {
            UUID categoryId = requested.getId();
            Optional<ExpenseCategory> foundCategory = categoryId == null
                    ? Optional.empty() : expenseCategoryRepo.findById(categoryId);
            if (foundCategory.isEmpty()) {
                return CATEGORY_NOT_FOUND;
            }
            categories.add(foundCategory.get());
        }
        target.setStall(stall);
        target.setExpenseCategories(categories);
        return null;
    }
}
