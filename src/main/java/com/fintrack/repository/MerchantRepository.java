package com.fintrack.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fintrack.domain.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

    boolean existsByStallId(UUID stallId);

    boolean existsByExpenseCategoriesId(UUID expenseCategoryId);
}
