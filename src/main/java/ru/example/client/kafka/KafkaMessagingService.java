package ru.example.client.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.example.client.model.ClientsAllInfo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaMessagingService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendDataToKafka(List<ClientsAllInfo> clients) {
        String message = "{\"data\":" + clients + "}\n";
        kafkaTemplate.send("test",0,null, message);
    }
}

