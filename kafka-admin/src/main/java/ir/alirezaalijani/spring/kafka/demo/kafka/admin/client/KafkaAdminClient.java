package ir.alirezaalijani.spring.kafka.demo.kafka.admin.client;

import ir.alirezaalijani.spring.kafka.demo.data.KafkaConfigData;
import ir.alirezaalijani.spring.kafka.demo.data.RetryConfigData;
import ir.alirezaalijani.spring.kafka.demo.kafka.admin.exception.KafkaAdminException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaAdminClient {

    private final KafkaConfigData kafkaConfigData;

    private final AdminClient adminClient;

    private final RetryTemplate retryTemplate;

    private final RetryConfigData retryConfigData;

    public KafkaAdminClient(KafkaConfigData config,
                            AdminClient client,
                            RetryTemplate template, RetryConfigData retryConfigData) {
        this.kafkaConfigData = config;
        this.adminClient = client;
        this.retryTemplate = template;
        this.retryConfigData = retryConfigData;
    }

    public void createTopics() {
        CreateTopicsResult createTopicsResult;
        try {
            List<String> topicNames = kafkaConfigData.getTopicNamesToCreate();
            log.info("Creating {} topics(s)", topicNames.size());
            List<NewTopic> kafkaTopics = topicNames.stream()
                    .map(topic -> TopicBuilder.name(topic.trim())
                            .partitions(kafkaConfigData.getNumOfPartitions())
                            .replicas(kafkaConfigData.getReplicationFactor())
                            .build()
                    ).collect(Collectors.toList());
            createTopicsResult = retryTemplate.execute(retryContext ->{
                log.info("----------Retry to create Topics-----------");
                log.info("Topics - list {}",kafkaTopics);
                return adminClient.createTopics(kafkaTopics);
            });
            log.info("Create topic result {}", createTopicsResult.values().values());
        } catch (Throwable t) {
            throw new KafkaException("Reached max number of retry for creating kafka topic(s)!", t);
        }
        checkTopicsCreated();
    }
    public void checkTopicsCreated() {
        Collection<TopicListing> topics = getTopics();
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        for (String topic : kafkaConfigData.getTopicNamesToCreate()) {
            while (!isTopicCreated(topics, topic)) {
                checkMaxRetry(retryCount++, maxRetry);
                sleep(sleepTimeMs);
                sleepTimeMs *= multiplier;
                topics = getTopics();
            }
        }
    }
    private void checkMaxRetry(int retry, Integer maxRetry) {
        if (retry > maxRetry) {
            throw new KafkaAdminException("Reached max number of retry for reading kafka topic(s)!");
        }
    }
    private void sleep(Long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new KafkaAdminException("Error while sleeping for waiting new created topics!!");
        }
    }

    private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
        if (topics == null) {
            return false;
        }
        boolean result=topics.stream().anyMatch(topic -> topic.name().equals(topicName));
        log.info("Topic {} is create result is {}",topicName,result);
        return result;
    }

    private Collection<TopicListing> getTopics() {
        Collection<TopicListing> topics;
        try {
            log.info("Reading kafka topic {}",kafkaConfigData.getTopicNamesToCreate().toArray());
            topics = retryTemplate.execute(retryContext -> {
                log.info("-----------Retry to get topic list-----------");
                return adminClient.listTopics().listings().get();
            });
            if (topics != null) {
                topics.forEach(topic -> log.debug("Topic with name {}", topic.name()));
            }
        } catch (Throwable t) {
            throw new KafkaException("Reached max number of retry for reading kafka topic(s)!", t);
        }
        return topics;
    }
}
