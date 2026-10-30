package com.fintrack.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fintrack.domain.Stall;

@Repository
public interface StallRepository extends JpaRepository<Stall, UUID> {

    boolean existsByStallNumber(int stallNumber);

    boolean existsByStallNumberAndIdNot(int stallNumber, UUID id);
}
