package org.example.walletConsumer.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.walletConsumer.models.Wallet;
import org.example.walletConsumer.repository.WalletRepository;
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
        wallet.setAmount(wallet.getAmount() + amount);
        walletRepository.save(wallet);
        return wallet;
    }

    //Данный метод позволяет снять деньги с кошелька
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Wallet withdrawMoney(int walletId, double amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new EntityNotFoundException("Такого кошелька не существует"));
        double newBalance = wallet.getAmount() - amount;
        if (newBalance < 0) {
            throw new IllegalArgumentException("Недостаточно средств на кошельке");
        }
        wallet.setAmount(newBalance);
        walletRepository.save(wallet);
        return wallet;
    }

    //Данный метод позволяет показать кошелек
    public Wallet getWallet(int walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new EntityNotFoundException("Такого кошелька не существует"));

        return wallet;
    }
}
