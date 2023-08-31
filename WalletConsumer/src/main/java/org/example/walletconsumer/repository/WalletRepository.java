package org.example.walletconsumer.repository;

import jakarta.persistence.LockModeType;
import org.example.walletconsumer.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findById(Integer integer);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Wallet save(Wallet wallet);

    Optional<Wallet>findAllById(Integer integer);
}
