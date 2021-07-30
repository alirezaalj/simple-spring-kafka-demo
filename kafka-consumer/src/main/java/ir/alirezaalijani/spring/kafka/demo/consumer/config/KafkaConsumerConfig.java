package ir.alirezaalijani.spring.kafka.demo.consumer.config;

import ir.alirezaalijani.spring.kafka.demo.data.KafkaConfigData;
import ir.alirezaalijani.spring.kafka.demo.model.Hobbies;
import ir.alirezaalijani.spring.kafka.demo.model.UserInfo;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "ir.alirezaalijani.spring.kafka.demo")
public class KafkaConsumerConfig {

    private final KafkaConfigData kafkaConfigData;

    public KafkaConsumerConfig(KafkaConfigData kafkaConfigData) {
        this.kafkaConfigData = kafkaConfigData;
    }

    @Bean
    public ConsumerFactory<Long, UserInfo> consumerFactory() {
        JsonDeserializer<UserInfo> deserializer = new JsonDeserializer<>(UserInfo.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("ir.alirezaalijani.spring.kafka.demo.model.*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        DefaultKafkaConsumerFactory<Long,UserInfo> consumerFactory=new DefaultKafkaConsumerFactory<>(config);
        consumerFactory.setValueDeserializer(deserializer);
        return consumerFactory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, UserInfo> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, UserInfo> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

}
