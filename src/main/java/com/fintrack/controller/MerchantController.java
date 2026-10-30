package com.fintrack.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintrack.domain.Merchant;
import com.fintrack.service.MerchantService;

@RestController
@RequestMapping(value = "/api/merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveMerchant(@RequestBody Merchant merchant) {

        String message = merchantService.saveMerchant(merchant);

        if (message.equals(MerchantService.STALL_NOT_FOUND) || message.equals(MerchantService.CATEGORY_NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        return new ResponseEntity<>(merchantService.getAllMerchants(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getMerchant(@PathVariable UUID id) {

        Optional<Merchant> merchant = merchantService.getMerchant(id);

        if (merchant.isEmpty()) {
            return new ResponseEntity<>(MerchantService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(merchant.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateMerchant(@PathVariable UUID id, @RequestBody Merchant merchant) {

        String message = merchantService.updateMerchant(id, merchant);

        if (message.equals(MerchantService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals(MerchantService.STALL_NOT_FOUND) || message.equals(MerchantService.CATEGORY_NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteMerchant(@PathVariable UUID id) {

        String message = merchantService.deleteMerchant(id);

        if (message.equals(MerchantService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.equals(MerchantService.IN_USE)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
