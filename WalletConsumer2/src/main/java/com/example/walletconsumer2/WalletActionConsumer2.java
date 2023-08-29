package com.example.walletconsumer2;

import com.example.walletconsumer2.service.WalletService;
import com.example.walletconsumer2.wallet.WalletJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class WalletActionConsumer2 {

    private final WalletService walletService;
    private final ObjectMapper objectMapper;

    public WalletActionConsumer2(WalletService walletService, ObjectMapper objectMapper) {
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