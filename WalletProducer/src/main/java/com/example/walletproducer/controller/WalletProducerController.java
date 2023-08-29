package com.example.walletproducer.controller;


import com.example.walletproducer.model.WalletEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WalletProducerController {

    private final KafkaTemplate<String, WalletEvent> kafkaTemplate;
    private static final String TOPIC = "wallet-events";


    public WalletProducerController(KafkaTemplate<String, WalletEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/api/producer")
    public String depositMoney(@RequestBody WalletEvent walletEvent) {
            if (walletEvent.getAction().equals("Deposit")||walletEvent.getAction().equals("Withdraw")){
                kafkaTemplate.send(TOPIC, walletEvent);
                System.out.println(walletEvent);
                return "Запрос отправлен в кафку: " + walletEvent;
            }
            else return null;
    }
}
