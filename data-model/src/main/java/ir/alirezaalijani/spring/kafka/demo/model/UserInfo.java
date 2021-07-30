package ir.alirezaalijani.spring.kafka.demo.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private List<Hobbies> hobbies;
}
