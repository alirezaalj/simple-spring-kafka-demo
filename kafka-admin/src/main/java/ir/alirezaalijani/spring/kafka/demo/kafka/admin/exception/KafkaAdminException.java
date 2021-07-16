package ir.alirezaalijani.spring.kafka.demo.kafka.admin.exception;

public class KafkaAdminException extends RuntimeException{
    public KafkaAdminException() {
        super();
    }

    public KafkaAdminException(String message) {
        super(message);
    }

    public KafkaAdminException(String message, Throwable cause) {
        super(message, cause);
    }
}
