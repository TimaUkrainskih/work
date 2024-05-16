package ru.example.client.services;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import ru.example.client.model.ClientsAllInfo;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientDataKafkaSender implements ApplicationRunner {

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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        send();
    }
}
