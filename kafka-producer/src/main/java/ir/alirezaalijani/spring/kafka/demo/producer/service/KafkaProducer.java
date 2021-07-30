package ir.alirezaalijani.spring.kafka.demo.producer.service;

import java.io.Serializable;

public interface KafkaProducer<K extends Serializable, V extends Serializable> {
    void send(String topicName, K key, V message);
}
