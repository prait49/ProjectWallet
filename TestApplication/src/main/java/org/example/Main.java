package org.example;


import org.example.models.WalletJson;
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

                    WalletJson walletJson = new WalletJson("Deposit",walletId, amountToDeposit);
                    HttpEntity<WalletJson> request = new HttpEntity<>(walletJson, headers);

                    String url = "http://localhost:8080/api/producer";
                    try {
                        String response = restTemplate.postForObject(url, request, String.class);
                        System.out.println("Thread " + threadNum + " - Request " + i + ": " + response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
    }
}
