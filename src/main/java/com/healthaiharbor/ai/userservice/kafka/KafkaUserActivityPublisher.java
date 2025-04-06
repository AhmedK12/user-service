package com.healthaiharbor.ai.userservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka-based implementation of the {@link ActivityPublisher}.
 * This class publishes user activity events to a specified Kafka topic.
 */
@Service
public class KafkaUserActivityPublisher implements ActivityPublisher {

    private static final Logger logger = LoggerFactory.getLogger(KafkaUserActivityPublisher.class);

    private final KafkaTemplate<Object, String> kafkaTemplate;
    private final String topic;
    private final ObjectMapper objectMapper;

    /**
     * Constructor to initialize Kafka publisher with a Kafka template and topic name.
     *
     * @param kafkaTemplate Kafka template for sending messages.
     * @param topic         Kafka topic name for user activity events.
     * @param objectMapper  JSON object mapper for serializing events.
     */
    public KafkaUserActivityPublisher(
            KafkaTemplate<Object, String> kafkaTemplate,
            @Value("${app.kafka.topic.user-activity}") String topic,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.objectMapper = objectMapper;
    }

    /**
     * Publishes a user activity event to Kafka.
     *
     * @param event The user activity event to publish.
     */
    @Override
    public void publish(UserActivityEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, eventJson);
            logger.info("Published event to Kafka: {}", eventJson);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize UserActivityEvent: {}", event, e);
        } catch (Exception e) {
            logger.error("Failed to publish UserActivityEvent to Kafka", e);
        }
    }
}
