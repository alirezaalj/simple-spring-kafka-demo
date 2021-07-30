package ir.alirezaalijani.spring.kafka.demo.webtokafka.controller;

import ir.alirezaalijani.spring.kafka.demo.model.UserInfo;
import ir.alirezaalijani.spring.kafka.demo.producer.service.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserDataController {

    private final KafkaProducer<Long, UserInfo> userInfoKafkaProducer;
    private final Random random;
    public UserDataController(KafkaProducer<Long, UserInfo> userInfoKafkaProducer) {
        this.userInfoKafkaProducer = userInfoKafkaProducer;
        random = new Random();
    }

    @PostMapping("/push")
    public ResponseEntity<?> pushUserData(@RequestBody UserInfo userInfo){
        long newId= random.nextInt();
        userInfo.setId(newId);
        userInfo.setUsername(userInfo.getUsername()+newId);
        userInfo.setFullName(userInfo.getFullName()+newId);
        userInfoKafkaProducer.send("user-topic",userInfo.getId(),userInfo);
        return ResponseEntity.ok("User push by id "+userInfo.getId());
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(HttpServletRequest request){
        return ResponseEntity.ok(request.getRemoteAddr());
    }
}
