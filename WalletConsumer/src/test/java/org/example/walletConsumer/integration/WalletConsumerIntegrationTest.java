package org.example.walletConsumer.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.WalletJson;
import org.example.walletConsumer.models.Wallet;
import org.example.walletConsumer.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(topics = {"wallet-events"}, partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:29092","port=9092"})
public class WalletConsumerIntegrationTest {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @Autowired
    private WalletRepository walletRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDepositMoney() throws Exception {
        WalletJson walletJson = new WalletJson("Deposit", 1, 10.0);
        kafkaTemplate.send("wallet-events", objectMapper.writeValueAsString(walletJson));

        // Небольшая пауза, чтобы дать Kafka Consumer время на обработку сообщения
        Thread.sleep(2000);

        Wallet wallet = walletRepository.findById(1).orElse(null);
        assertNotNull(wallet);
        assertEquals(10.0, wallet.getAmount(), 0.001);
    }

    @Test
    public void testWithdrawMoneyNotEnoughFunds() throws Exception {
        WalletJson walletJson = new WalletJson("Withdraw", 1, 50.0);
        kafkaTemplate.send("wallet-events", objectMapper.writeValueAsString(walletJson));

        Thread.sleep(2000);

        // Поскольку у нас не хватает средств для снятия, мы ожидаем, что изменений в базе данных не произойдет
        Wallet wallet = walletRepository.findById(1).orElse(null);
        assertNotNull(wallet);
        assertNotEquals(-40.0, wallet.getAmount(), 0.001);
    }


}

