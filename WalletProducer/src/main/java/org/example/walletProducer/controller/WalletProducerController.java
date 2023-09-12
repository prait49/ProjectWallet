package org.example.walletProducer.controller;

import org.example.models.WalletJson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;


@RestController
public class WalletProducerController {

    private final KafkaTemplate<String, WalletJson> kafkaTemplate;
    private static final String TOPIC = "wallet-events";
    private AtomicInteger messageKey = new AtomicInteger(0);

    public WalletProducerController(KafkaTemplate<String, WalletJson> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/api/producer")
    public ResponseEntity<String> depositMoney(@RequestBody WalletJson walletJson) {
            if (walletJson.getAction().equals("Deposit")|| walletJson.getAction().equals("Withdraw")){

                int key = messageKey.getAndIncrement();
                String keyString = Integer.toString(key);
                System.out.println(keyString);
                kafkaTemplate.send(TOPIC, keyString, walletJson);
                System.out.println(walletJson);
                return ResponseEntity.ok("Запрос отправлен в кафку: " + walletJson);
            }
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Выберите правильную операцию.");
    }
}
