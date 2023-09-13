package org.example.walletConsumer.service;

import org.example.walletConsumer.models.Wallet;
import org.example.walletConsumer.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Import(WalletService.class)
public class WalletServiceTest {
    //Переписать на связь с БД
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;


    @BeforeEach
    public void setUp() {
        walletRepository.save(new Wallet(1, 100.0));
    }

    @Test
    public void testDepositMoney() {
        // подготовка
        int walletId = 1;
        double amount = 10.0;
        // вызов метода
        walletService.depositMoney(walletId, amount);
        // проверка
        Wallet wallet =walletService.getWallet(1);
        assertEquals(110,wallet.getAmount());
    }

    @Test
    public void testWithdrawMoneyNotEnoughFunds() {
        int walletId = 1;
        double amountToWithdraw = 150.0; // больше, чем имеющийся баланс кошелька
        // Ожидаем, что при попытке снять больше, чем есть на балансе, будет выброшено исключение
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            walletService.withdrawMoney(walletId, amountToWithdraw);
        });
        // Проверим, что сообщение исключения соответствует ожидаемому
        String expectedMessage = "Недостаточно средств на кошельке";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        // Также убедимся, что баланс кошелька не изменился
        Wallet wallet = walletService.getWallet(walletId);
        assertEquals(100.0, wallet.getAmount());
    }
}