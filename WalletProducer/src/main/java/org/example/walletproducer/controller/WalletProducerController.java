package org.example.walletproducer.controller;

import org.example.models.WalletJson;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WalletProducerController {

    private final KafkaTemplate<String, WalletJson> kafkaTemplate;
    private static final String TOPIC = "wallet-events";


    public WalletProducerController(KafkaTemplate<String, WalletJson> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/api/producer")
    public String depositMoney(@RequestBody WalletJson walletJson) {
            if (walletJson.getAction().equals("Deposit")|| walletJson.getAction().equals("Withdraw")){
                kafkaTemplate.send(TOPIC, walletJson);
                System.out.println(walletJson);
                return "Запрос отправлен в кафку: " + walletJson;
            }
            else return null;
    }
}
