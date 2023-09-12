package org.example.walletProducer.controller;

import org.example.models.WalletJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class WalletProducerControllerTest {

    @Mock
    private KafkaTemplate<String, WalletJson> kafkaTemplate;

    @InjectMocks
    private WalletProducerController controller;

    @Test
    public void testValidRequest() {
        // подготовка данных
        WalletJson walletJson = new WalletJson("Deposit", 1, 10.0);

        // вызов метода
        controller.depositMoney(walletJson);

        // верификация
        Mockito.verify(kafkaTemplate)
                .send(eq("wallet-events"), anyString(), eq(walletJson));
    }

    @Test
    public void testInvalidRequest() {
        // подготовка данных
        WalletJson invalidJson = new WalletJson("Invalid", 1, 10.0);

        // вызов метода
        String result = String.valueOf(controller.depositMoney(invalidJson));

        // проверка результата
        assertNull(result);
        Mockito.verifyNoInteractions(kafkaTemplate);
    }
}