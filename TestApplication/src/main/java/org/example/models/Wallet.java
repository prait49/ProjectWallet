package org.example.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Wallet {
    private int id;
    private double amount;
    private String action;
}
