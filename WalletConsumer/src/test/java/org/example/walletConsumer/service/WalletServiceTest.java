package org.example.walletConsumer.service;

import org.example.walletConsumer.models.Wallet;
import org.example.walletConsumer.repository.WalletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    public void testDepositMoney() {
        // подготовка
        int walletId = 1;
        double amount = 10.0;
        Wallet wallet = new Wallet(walletId, 20.0);
        Mockito.when(walletRepository.findById(walletId))
                .thenReturn(Optional.of(wallet));
        // вызов метода
        Wallet result = walletService.depositMoney(walletId, amount);
        // проверка
        Assertions.assertEquals(30.0, result.getAmount());
        Mockito.verify(walletRepository).save(wallet);
    }

    @Test
    public void testWithdrawMoneyNotEnoughFunds() {
        // подготовка
        int walletId = 1;
        double amount = 50.0;
        // Настроить мок для выбрасывания исключения при вызове findById
        Mockito.when(walletRepository.findById(walletId))
                .thenThrow(new IllegalArgumentException("Not enough funds"));
        // вызов метода
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            walletService.withdrawMoney(walletId, amount);
        });
        // проверка
        Mockito.verify(walletRepository).findById(walletId);
    }}