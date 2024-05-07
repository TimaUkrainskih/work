package ru.example.client.kafka;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KafkaController {
    private final KafkaMessagingService kafkaMessagingService;

    @GetMapping("/send")
    public void senData(){
        kafkaMessagingService.sendData();
    }


}
