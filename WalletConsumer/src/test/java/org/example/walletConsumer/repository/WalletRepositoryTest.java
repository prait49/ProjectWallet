package org.example.walletConsumer.repository;


import org.example.walletConsumer.models.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;


    @BeforeEach
    public void setUp() {
        walletRepository.save(new Wallet(1, 100.0));
    }
    @Test
    public void testFindById() {

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

}

