package ir.alirezaalijani.spring.kafka.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hobbies implements Serializable {
    private String name;
    private Integer rate;
}
