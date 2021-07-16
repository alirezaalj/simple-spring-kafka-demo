package ir.alirezaalijani.spring.kafka.demo.data;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "web-to-kafka")
public class WebToKafkaConfigData {
    private String helloMessage;
}
