package org.example;

import org.example.models.Wallet;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int numThreads = 6; // Количество потоков
        int requestsPerThread = 500; // Количество запросов на каждом потоке
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (int threadId = 0; threadId < numThreads; threadId++) {
            final int threadNum = threadId;
            executorService.execute(() -> {
                for (int i = 0; i < requestsPerThread; i++) {
                    int walletId = 1; // Идентификатор кошелька
                    double amountToDeposit = 10; // Сумма депозита

                    Wallet wallet = new Wallet(walletId, amountToDeposit, "Deposit");
                    HttpEntity<Wallet> request = new HttpEntity<>(wallet, headers);

                    String url = "http://localhost:8080/api/producer";
                    try {
                        String response = restTemplate.postForObject(url, request, String.class);
                        System.out.println("Thread " + threadNum + " - Request " + i + ": " + response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
    }
}
