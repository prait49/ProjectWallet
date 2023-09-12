package org.example.walletProducer.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.models.WalletJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"wallet-events"}, brokerProperties = {"listeners=PLAINTEXT://localhost:29092","port=9092"})
public class WalletProducerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;


    @Test
    public void testInvalidRequestReturnsUnauthorized() throws Exception {
        WalletJson invalidRequest = new WalletJson("InvalidAction", 1, 100.0);
        mockMvc.perform(post("/api/producer")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testValidRequestIsSentToKafka() throws Exception {
        WalletJson validRequest = new WalletJson("Deposit", 1, 100.0);
        ResultActions result = mockMvc.perform(post("/api/producer")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(validRequest)))
                .andExpect(status().isOk());

        String responseContent = result.andReturn().getResponse().getContentAsString();
        assertTrue(responseContent.contains("Запрос отправлен в кафку"));
    }

    @Test
    public void testRequestIsSentToKafkaOnlyOnce() throws Exception {
        WalletJson validRequest = new WalletJson("Withdraw", 1, 50.0);
        mockMvc.perform(post("/api/producer")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(validRequest)))
                .andExpect(status().isOk());
        Consumer<String, WalletJson> consumer = createConsumer();
        consumer.subscribe(Collections.singletonList("wallet-events"));
        ConsumerRecords<String, WalletJson> records = consumer.poll(Duration.ofSeconds(5));
        int recordCount = records.count();
        // Ожидается только одна запись
        assertEquals(1, recordCount);
        consumer.close();
        // Получаем первую и единственную запись
        WalletJson receivedWalletJson = records.iterator().next().value();

        // Проверяем, что полученное сообщение соответствует отправленному
        assertEquals(validRequest.getAction(), receivedWalletJson.getAction());
        assertEquals(validRequest.getId(), receivedWalletJson.getId());
        assertEquals(validRequest.getAmount(), receivedWalletJson.getAmount(), 0.001);
    }

    private Consumer<String, WalletJson> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, WalletJson.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaConsumer<>(props);
    }
}



