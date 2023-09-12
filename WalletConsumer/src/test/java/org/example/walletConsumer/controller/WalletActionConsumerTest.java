package org.example.walletConsumer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.WalletJson;
import org.example.walletConsumer.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(topics = {"wallet-events"}, partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:29093","port=9092"})
@TestPropertySource(properties = {"spring.kafka.bootstrap-servers=localhost:29093"})
public class WalletActionConsumerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private WalletService walletService;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testConsumeDepositWalletEvent() throws Exception {
        WalletJson walletJson = new WalletJson("Deposit", 1, 10.0);
        kafkaTemplate.send("wallet-events", objectMapper.writeValueAsString(walletJson));
        Thread.sleep(2000);
        verify(walletService, times(1)).depositMoney(1, 10.0);
    }

    @Test
    public void testConsumeWithdrawWalletEvent() throws Exception {
        WalletJson walletJson = new WalletJson("Withdraw", 1, 50.0);
        kafkaTemplate.send("wallet-events", objectMapper.writeValueAsString(walletJson));
        Thread.sleep(2000);
        verify(walletService, times(1)).withdrawMoney(1, 50.0);
    }
}
