package com.example.walletconsumer;

import com.example.walletconsumer.service.WalletService;
import com.example.walletconsumer.wallet.WalletJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class WalletActionConsumer {

    private final WalletService walletService;
    private final ObjectMapper objectMapper;

    public WalletActionConsumer(WalletService walletService, ObjectMapper objectMapper) {
        this.walletService = walletService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "wallet-events", groupId = "wallet-action-consumer")
    public void consumeWalletEvent(String eventJson) {
        try {
            WalletJson walletJson = objectMapper.readValue(eventJson, WalletJson.class);
            String action = walletJson.getAction();
            int walletId = walletJson.getId();
            double amount = walletJson.getAmount();
            if (action.equals("Deposit")) {
                System.out.println(walletService.depositMoney(walletId, amount));
            } else if (action.equals("Withdraw")) {
                System.out.println(walletService.withdrawMoney(walletId, amount));
            }

        } catch (Exception e) {
            System.out.println("ОШИБКА!!!");
        }
    }
}