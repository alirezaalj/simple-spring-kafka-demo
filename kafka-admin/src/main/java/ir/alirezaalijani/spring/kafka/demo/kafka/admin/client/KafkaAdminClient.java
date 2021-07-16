package ir.alirezaalijani.spring.kafka.demo.kafka.admin.client;

import ir.alirezaalijani.spring.kafka.demo.data.KafkaConfigData;
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


    public KafkaAdminClient(KafkaConfigData config,
                            AdminClient client,
                            RetryTemplate template) {
        this.kafkaConfigData = config;
        this.adminClient = client;
        this.retryTemplate = template;
    }

    public void createTopics() {
        CreateTopicsResult createTopicsResult;
        try {
            List<String> topicNames = kafkaConfigData.getTopicNamesToCreate();
            log.info("Creating {} topics(s)", topicNames.size());
            List<NewTopic> kafkaTopics = topicNames.stream()
                    .map(topic -> TopicBuilder.name(topic.trim())
                            .partitions(kafkaConfigData.getNumOfPartitions())
                            .build()
                    ).collect(Collectors.toList());
            createTopicsResult = retryTemplate.execute(retryContext ->{
                log.info("----------Retry to create Topics-----------");
                return adminClient.createTopics(kafkaTopics);
            });
            log.info("Create topic result {}", createTopicsResult.values().values());
        } catch (Throwable t) {
            throw new KafkaException("Reached max number of retry for creating kafka topic(s)!", t);
        }
        checkTopicsCreated();
    }
    private void checkTopicsCreated() {
        Collection<TopicListing> topics = getTopics();
        for (String topic : kafkaConfigData.getTopicNamesToCreate()) {
            if(isTopicCreated(topics, topic)){
                log.info("Topic {} is created.",topic);
            }else {
                log.info("Topic {} dose not created.",topic);
            }
        }
    }
    private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
        if (topics == null) {
            return false;
        }
        return topics.stream().anyMatch(topic -> topic.name().equals(topicName));
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
