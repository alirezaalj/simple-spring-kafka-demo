package ir.alirezaalijani.spring.kafka.demo.consumer.services;

import ir.alirezaalijani.spring.kafka.demo.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "user-topic", groupId = "consumer_group")
    public void consume(UserInfo message) {
        log.info(String.format("$$$$ => Consumed message: %s", message));
    }

}
