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

import com.fintrack.domain.Stall;
import com.fintrack.service.StallService;

@RestController
@RequestMapping(value = "/api/stalls")
public class StallController {

    @Autowired
    private StallService stallService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveStall(@RequestBody Stall stall) {

        String message = stallService.saveStall(stall);

        if (message.equals(StallService.NUMBER_TAKEN)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Stall>> getAllStalls() {
        return new ResponseEntity<>(stallService.getAllStalls(), HttpStatus.OK);
    }

    @GetMapping(value = "/free", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Stall>> getFreeStalls() {
        return new ResponseEntity<>(stallService.getFreeStalls(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getStall(@PathVariable UUID id) {

        Optional<Stall> stall = stallService.getStall(id);

        if (stall.isEmpty()) {
            return new ResponseEntity<>(StallService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stall.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStall(@PathVariable UUID id, @RequestBody Stall stall) {

        String message = stallService.updateStall(id, stall);

        if (message.equals(StallService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals(StallService.NUMBER_TAKEN)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteStall(@PathVariable UUID id) {

        String message = stallService.deleteStall(id);

        if (message.equals(StallService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.equals(StallService.IN_USE)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
