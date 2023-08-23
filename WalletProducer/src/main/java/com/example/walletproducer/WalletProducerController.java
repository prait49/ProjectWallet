package com.example.walletproducer;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "wallet-events";

    public WalletProducerController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/api/producer/deposit")
    public String depositMoney(@RequestParam int walletId, @RequestParam double amount) {
        String event ="Deposit "+amount+" "+walletId;
        kafkaTemplate.send(TOPIC, event);
        return "Запрос на внесение отправлен в кафку: " + event;
    }

    @PostMapping("/api/producer/withdraw")
    public String withdrawMoney(@RequestParam int walletId, @RequestParam double amount) {
        String event = "Withdrawal of " + amount + " from wallet " + walletId;
        kafkaTemplate.send(TOPIC, event);
        return "Withdrawal event sent to Kafka: " + event;
    }
}
