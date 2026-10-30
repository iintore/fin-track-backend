package com.fintrack.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fintrack.domain.ReceiptLine;
import com.fintrack.service.ReceiptLineService;

@RestController
@RequestMapping(value = "/api/receipt-lines")
public class ReceiptLineController {

    @Autowired
    private ReceiptLineService receiptLineService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveReceiptLine(@RequestParam UUID expenseId, @RequestBody ReceiptLine receiptLine) {

        String message = receiptLineService.saveReceiptLine(expenseId, receiptLine);

        if (message.equals(ReceiptLineService.EXPENSE_NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.equals(ReceiptLineService.NOT_SETTLED)) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/account-holder/{walletNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiptLine>> getReceiptLinesOfAccountHolder(@PathVariable String walletNumber) {
        return new ResponseEntity<>(receiptLineService.getReceiptLinesOfAccountHolder(walletNumber), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteReceiptLine(@PathVariable UUID id) {

        String message = receiptLineService.deleteReceiptLine(id);

        if (message.equals(ReceiptLineService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
