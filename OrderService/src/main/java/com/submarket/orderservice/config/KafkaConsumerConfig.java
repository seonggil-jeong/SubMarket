package com.submarket.orderservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final Environment env;

    /**
     * Consumer : 관심 정보를 구독 (정보를 받는 Service)
     */

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.ip")); // ip Address
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerOrder"); // Group 으로 묶기
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // topic Type (Key)
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // topic Type (Value)

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    // Event 발생을 대기하는 Listener with Topic
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();

        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());

        return kafkaListenerContainerFactory;

    }
}
