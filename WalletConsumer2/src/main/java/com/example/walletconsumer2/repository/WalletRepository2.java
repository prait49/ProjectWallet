package com.example.walletconsumer2.repository;

import com.example.walletconsumer2.wallet.Wallet;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository2 extends JpaRepository<Wallet,Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findById(Integer integer);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    Wallet save(Wallet wallet);

    Optional<Wallet>findAllById(Integer integer);
}
