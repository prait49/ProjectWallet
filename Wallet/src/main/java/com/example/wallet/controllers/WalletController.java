package com.example.wallet.controllers;


import com.example.wallet.models.Wallet;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/{walletId}/deposit")
    public Wallet depositMoney(@PathVariable int walletId, @RequestParam double amount){
        return walletService.depositMoney(walletId, amount);
    }

    @PostMapping("/{walletId}/withdraw")
    public Wallet withdrawMoney(@PathVariable int walletId, @RequestParam double amount){
        return walletService.withdrawMoney(walletId,amount);
    }

    @GetMapping("/{walletId}")
    public Wallet getWallet(@PathVariable int walletId){
        return walletService.getWallet(walletId);
    }

}
