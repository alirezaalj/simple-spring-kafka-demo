package ir.alirezaalijani.spring.kafka.demo.producer.service;


import ir.alirezaalijani.spring.kafka.demo.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.Serializable;

@Slf4j
@Service
public final class ProducerService implements KafkaProducer<Long,UserInfo>{

    private final KafkaTemplate<Long, UserInfo> kafkaTemplate;

    public ProducerService(KafkaTemplate<Long, UserInfo> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, Long key, UserInfo message) {
        log.info(String.format("$$$$ => Producing message: %s", message));

        ListenableFuture<SendResult<Long, UserInfo>> kafkaResultFuture =
                kafkaTemplate.send(topicName,key, message);
        addCallback(topicName, message, kafkaResultFuture);
    }

    private void addCallback(String topicName, UserInfo message,
                             ListenableFuture<SendResult<Long, UserInfo>> kafkaResultFuture) {
        kafkaResultFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("Error while sending message {} to topic {}", message.toString(), topicName, throwable);
            }

            @Override
            public void onSuccess(SendResult<Long, UserInfo> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.debug("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime());
            }
        });
    }


}
