package ru.example.client.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.client.model.ClientsAllInfo;
import ru.example.client.services.ClientsService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientDataKafkaSender{

    private final KafkaMessagingService kafkaMessagingService;

    private final ClientsService clientsService;

    public void send() {
        int offset = 0;
        while (true) {
            List<ClientsAllInfo> clients = clientsService.clientsAllInfoList(offset);
            if (clients.isEmpty()) break;
            kafkaMessagingService.sendDataToKafka(clients);
            if (clients.size() < 5) break;
            offset += 5;
        }
    }
}
