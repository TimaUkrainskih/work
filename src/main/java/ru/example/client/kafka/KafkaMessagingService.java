package ru.example.client.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.example.client.model.ClientsAllInfo;
import ru.example.client.repository.ClientsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaMessagingService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ClientsRepository clientsRepository;

    @PostConstruct
    public void sendData() {
        int offset = 0;
        while (true) {
            List<ClientsAllInfo> clients = clientsRepository.clients(offset);
            if (clients.isEmpty()) break;
            String message = "{\"data\":" + clients + "}\n";
            kafkaTemplate.send("test", message);
            if (clients.size() < 5) break;
            offset += 5;
        }
    }
}
