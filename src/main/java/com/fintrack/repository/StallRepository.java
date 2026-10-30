package com.fintrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fintrack.domain.Stall;

@Repository
public interface StallRepository extends JpaRepository<Stall, UUID> {

    boolean existsByStallNumber(int stallNumber);

    boolean existsByStallNumberAndIdNot(int stallNumber, UUID id);

    @Query("SELECT l FROM Stall l WHERE NOT EXISTS (SELECT p FROM Merchant p WHERE p.stall = l) ORDER BY l.stallNumber")
    List<Stall> findFreeStalls();
}
