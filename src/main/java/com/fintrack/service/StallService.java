package com.fintrack.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintrack.domain.Stall;
import com.fintrack.repository.MerchantRepository;
import com.fintrack.repository.StallRepository;

@Service
public class StallService {

    public static final String NOT_FOUND = "The stall with that id does not exist";
    public static final String NUMBER_TAKEN = "A stall with that stallNumber already exists";
    public static final String IN_USE = "That stall has a merchant";

    @Autowired
    private StallRepository stallRepo;

    @Autowired
    private MerchantRepository merchantRepo;

    public List<Stall> getAllStalls() {
        return stallRepo.findAll();
    }

    public List<Stall> getFreeStalls() {
        return stallRepo.findFreeStalls();
    }

    public Optional<Stall> getStall(UUID id) {
        return stallRepo.findById(id);
    }

    public String saveStall(Stall stall) {
        if (stallRepo.existsByStallNumber(stall.getStallNumber())) {
            return NUMBER_TAKEN;
        }
        stallRepo.save(stall);
        return "Stall saved successfully";
    }

    public String updateStall(UUID id, Stall newData) {
        Optional<Stall> found = stallRepo.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }
        if (stallRepo.existsByStallNumberAndIdNot(newData.getStallNumber(), id)) {
            return NUMBER_TAKEN;
        }
        Stall stall = found.get();
        stall.setStallNumber(newData.getStallNumber());
        stall.setName(newData.getName());
        stallRepo.save(stall);
        return "Stall updated successfully";
    }

    public String deleteStall(UUID id) {
        if (!stallRepo.existsById(id)) {
            return NOT_FOUND;
        }
        if (merchantRepo.existsByStallId(id)) {
            return IN_USE;
        }
        stallRepo.deleteById(id);
        return "Stall deleted successfully";
    }
}
