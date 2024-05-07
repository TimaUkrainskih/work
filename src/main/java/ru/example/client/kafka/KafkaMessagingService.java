package ru.example.client.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.example.client.repository.ClientsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaMessagingService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ClientsRepository clientsRepository;

    public void sendData() {
        List<Map<String, Object>> clients = clientsRepository.clients();
        List<Object> data = new ArrayList<>();
        for (Map<String, Object> client : clients) {
            data.add(client);
        }
        sendToKafka(data);
    }

    private void sendToKafka(List<Object> data) {
        List<List<Object>> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i += 5) {
            int endIndex = Math.min(i + 5, data.size());
            result.add(data.subList(i, endIndex));
        }
        for (List<Object> res : result) {
            String message = "{\"data\":" + res.toString() + "}\n";
            kafkaTemplate.send("test", message);
        }
    }
}
