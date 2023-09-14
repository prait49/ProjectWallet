package org.example.walletConsumer.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.models.WalletJson;
import org.example.walletConsumer.controller.WalletActionConsumer;
import org.example.walletConsumer.models.Wallet;
import org.example.walletConsumer.repository.WalletRepository;
import org.example.walletConsumer.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class WalletConsumerIntegrationTestcontainer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

//    @Autowired
//    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletActionConsumer walletActionConsumer;

    private ObjectMapper objectMapper = new ObjectMapper();

    public static Network network = Network.newNetwork();

    @Container
    public static KafkaContainer kafka1 = new KafkaContainer("6.2.4")
            .withNetwork(network);

    @Container
    public static KafkaContainer kafka2 = new KafkaContainer("6.2.4")
            .withNetwork(network);


    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:alpine3.18")
            .withDatabaseName("wallet")
            .withUsername("postgres")
            .withPassword("2679")
            .withNetwork(network);

    @DynamicPropertySource
    public static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> kafka1.getBootstrapServers() + "," + kafka2.getBootstrapServers());
    }

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.liquibase.change-log", () -> "classpath:/db/changelog/master.yaml");
    }

    @Test
    public void testDepositMoney() throws Exception {
        WalletJson walletJson = new WalletJson("Deposit", 1, 10.0);
        kafkaTemplate.send("wallet-events", objectMapper.writeValueAsString(walletJson));
        Thread.sleep(2000);
        Wallet wallet = walletService.getWallet(1);
        assertNotNull(wallet);
        assertEquals(10.0, wallet.getAmount(), 0.001);
    }

    @Test
    public void testWithdrawMoneyNotEnoughFunds() throws Exception {
        WalletJson walletJson = new WalletJson("Withdraw", 1, 50.0);
        kafkaTemplate.send("wallet-events", objectMapper.writeValueAsString(walletJson));
        Thread.sleep(2000);
        Wallet wallet = walletService.getWallet(1);
        assertNotNull(wallet);
        assertNotEquals(-40.0, wallet.getAmount(), 0.001);
    }

}

