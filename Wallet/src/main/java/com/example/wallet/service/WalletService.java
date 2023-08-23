package com.example.wallet.service;

import com.example.wallet.models.Wallet;
import com.example.wallet.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class WalletService {


    private final WalletRepository walletRepository;


    //Данный метод позволяет внести деньги на кошелек
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Wallet depositMoney(int walletId, double amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new EntityNotFoundException("Такого кошелька не существует"));
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);

        return wallet;
    }

    //Данный метод позволяет снять деньги с кошелька
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Wallet withdrawMoney(int walletId, double amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new EntityNotFoundException("Такого кошелька не существует"));
        double newBalance = wallet.getBalance() - amount;
        if (newBalance < 0) {
            throw new IllegalArgumentException("Недостаточно средств на кошельке");
        }
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        return wallet;
    }

    //Данный метод позволяет показать кошелек
    public Wallet getWallet(int walletId) {
        return walletRepository.findAllById(walletId).orElseThrow(() -> new EntityNotFoundException("Такого кошелька не существует"));
    }
}
