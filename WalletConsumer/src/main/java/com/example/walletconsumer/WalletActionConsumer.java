package com.example.walletconsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WalletActionConsumer {

    private final RestTemplate restTemplate;
    private static final String WALLET_API_URL = "http://localhost:8081/api/wallets";

    public WalletActionConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
























    @KafkaListener(topics = "wallet-events", groupId = "wallet-action-consumer")
    public void consumeWalletEvent(String event) {
        // Обработка события из Kafka и отправка запроса на основной API
        String[] eventParts = event.split(" ");
        String action = eventParts[0];
        int walletId = Integer.parseInt(eventParts[2]);
        double amount = Double.parseDouble(eventParts[1]);

        // Отправка запроса на основной API в зависимости от действия
        String apiUrl = WALLET_API_URL + "/" + walletId;
        String response=null;
        if (action.equals("Deposit")) {
             response = restTemplate.postForObject(apiUrl + "/deposit?amount=" + amount, null, String.class);
        } else if (action.equals("Withdrawal")) {
            response = restTemplate.postForObject(apiUrl + "/withdraw?amount=" + amount, null, String.class);
        }
        System.out.println(response);
    }
}