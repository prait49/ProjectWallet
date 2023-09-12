package org.example.walletConsumer.repository;


import org.example.walletConsumer.models.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    public void testFindById() {
        Wallet savedWallet = new Wallet();
        savedWallet.setId(1);
        savedWallet.setAmount(100.0);
        walletRepository.save(savedWallet);
        Optional<Wallet> retrievedWallet = walletRepository.findById(1);
        assertTrue(retrievedWallet.isPresent());
        assertEquals(100.0, retrievedWallet.get().getAmount(), 0.001);
    }

    @Test
    public void testSave() {
        Wallet wallet = new Wallet();
        wallet.setId(2);
        wallet.setAmount(200.0);
        Wallet savedWallet = walletRepository.save(wallet);
        assertNotNull(savedWallet);
        assertEquals(2, savedWallet.getId());
        assertEquals(200.0, savedWallet.getAmount(), 0.001);
    }

    @Test
    public void testFindAllById() {
        Wallet wallet = new Wallet();
        wallet.setId(3);
        wallet.setAmount(300.0);
        walletRepository.save(wallet);
        Optional<Wallet> retrievedWallet = walletRepository.findAllById(3);
        assertTrue(retrievedWallet.isPresent());
        assertEquals(300.0, retrievedWallet.get().getAmount(), 0.001);
    }
}

