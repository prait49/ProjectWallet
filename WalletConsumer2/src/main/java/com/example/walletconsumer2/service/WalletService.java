package com.example.walletconsumer2.service;

import com.example.walletconsumer2.repository.WalletRepository2;
import com.example.walletconsumer2.wallet.Wallet;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WalletService {


    private final WalletRepository2 walletRepository2;


    //Данный метод позволяет внести деньги на кошелек
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Wallet depositMoney(int walletId, double amount) {
        Wallet wallet = walletRepository2.findById(walletId).orElseThrow(() -> new EntityNotFoundException("Такого кошелька не существует"));
        wallet.setAmount(wallet.getAmount() + amount);
        walletRepository2.save(wallet);
        return wallet;
    }

    //Данный метод позволяет снять деньги с кошелька
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Wallet withdrawMoney(int walletId, double amount) {
        Wallet wallet = walletRepository2.findById(walletId).orElseThrow(() -> new EntityNotFoundException("Такого кошелька не существует"));
        double newBalance = wallet.getAmount() - amount;
        if (newBalance < 0) {
            throw new IllegalArgumentException("Недостаточно средств на кошельке");
        }
        wallet.setAmount(newBalance);
        walletRepository2.save(wallet);
        return wallet;
    }

//    //Данный метод позволяет показать кошелек
//    public Wallet getWallet(int walletId) {
//        return walletRepository.findAllById(walletId).orElseThrow(() -> new EntityNotFoundException("Такого кошелька не существует"));
//    }
}
