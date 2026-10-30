package com.fintrack.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fintrack.domain.AccountHolder;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, UUID> {

    Optional<AccountHolder> findByWalletNumber(String walletNumber);

    List<AccountHolder> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT e.accountHolder FROM Expense e WHERE e.merchant.id = :merchantId")
    List<AccountHolder> findAccountHoldersOfMerchant(@Param("merchantId") UUID merchantId);
}
