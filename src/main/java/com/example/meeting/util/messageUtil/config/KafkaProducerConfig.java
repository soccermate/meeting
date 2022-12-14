package com.example.meeting.util.messageUtil.config;

import com.example.meeting.util.messageUtil.dto.PointEarnedMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private List<String> bootstrapServers;

    @Bean
    SenderOptions<Integer, PointEarnedMessage> getProducerOption()
    {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return SenderOptions.<Integer, PointEarnedMessage>create(producerProps)
                .maxInFlight(1024);
    }

    @Bean
    KafkaSender<Integer, PointEarnedMessage> getKafkaSender(SenderOptions<Integer, PointEarnedMessage> options)
    {
        return KafkaSender.create(options);
    }

}
