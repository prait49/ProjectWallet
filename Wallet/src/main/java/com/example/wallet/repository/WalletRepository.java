package com.example.wallet.repository;

import com.example.wallet.models.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findById(Integer integer);

    Optional<Wallet>findAllById(Integer integer);
}
