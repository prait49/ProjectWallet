package com.example.walletproducer;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ConfigKafka {

    @Bean
    public NewTopic topic(){
        return TopicBuilder.name("wallet-events")
                .partitions(10)
                .replicas(2)
                .build();
    }
}
