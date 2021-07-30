package ir.alirezaalijani.spring.kafka.demo.webtokafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "ir.alirezaalijani.spring.kafka.demo")
public class WebServiceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("starting web application");
    }
}
