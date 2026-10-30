package com.fintrack.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stall")
public class Stall {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "stall_number")
    private int stallNumber;

    @Column(name = "name")
    private String name;

    public UUID getId() {
        return id;
    }

    public int getStallNumber() {
        return stallNumber;
    }

    public void setStallNumber(int stallNumber) {
        this.stallNumber = stallNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
