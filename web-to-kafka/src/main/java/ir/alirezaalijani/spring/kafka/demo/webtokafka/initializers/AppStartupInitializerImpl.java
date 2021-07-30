package ir.alirezaalijani.spring.kafka.demo.webtokafka.initializers;

import ir.alirezaalijani.spring.kafka.demo.data.KafkaConfigData;
import ir.alirezaalijani.spring.kafka.demo.data.KafkaProducerConfigData;
import ir.alirezaalijani.spring.kafka.demo.data.RetryConfigData;
import ir.alirezaalijani.spring.kafka.demo.data.WebToKafkaConfigData;
import ir.alirezaalijani.spring.kafka.demo.kafka.admin.client.KafkaAdminClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppStartupInitializerImpl implements AppStartupInitializer {
    private final WebToKafkaConfigData webToKafkaConfigData;
    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final KafkaProducerConfigData producerConfigData;
    private final KafkaAdminClient kafkaAdminClient;
    public AppStartupInitializerImpl(WebToKafkaConfigData webToKafkaConfigData,
                                     KafkaConfigData kafkaConfigData,
                                     RetryConfigData retryConfigData,
                                     KafkaProducerConfigData producerConfigData, KafkaAdminClient kafkaAdminClient) {
        this.webToKafkaConfigData = webToKafkaConfigData;
        this.kafkaConfigData = kafkaConfigData;
        this.retryConfigData = retryConfigData;
        this.producerConfigData = producerConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init() {
        log.debug("Read configs for webToKafkaConfigData {}",webToKafkaConfigData.toString());
        log.debug("Read configs for kafkaConfigData {}",kafkaConfigData.toString());
        log.debug("Read configs for retryConfigData {}",retryConfigData.toString());
        log.debug("Read configs for producerConfigData {}",producerConfigData.toString());
        log.info("Start creating topics {}",kafkaConfigData.getTopicNamesToCreate());
        kafkaAdminClient.createTopics();
    }
}
