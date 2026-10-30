package com.fintrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fintrack.domain.Merchant;
import com.fintrack.domain.Stall;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

    boolean existsByStallId(UUID stallId);

    boolean existsByStallAndIdNot(Stall stall, UUID id);

    boolean existsByExpenseCategoriesId(UUID expenseCategoryId);

    @Query("SELECT m FROM Merchant m JOIN m.expenseCategories c WHERE LOWER(c.name) = LOWER(:name)")
    List<Merchant> findMerchantsByExpenseCategoryName(@Param("name") String name);
}
