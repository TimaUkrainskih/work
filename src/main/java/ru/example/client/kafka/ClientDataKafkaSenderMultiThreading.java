package ru.example.client.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.client.model.ClientsAllInfo;
import ru.example.client.services.ClientsService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RequiredArgsConstructor
@Service
public class ClientDataKafkaSenderMultiThreading {

    private final KafkaMessagingService kafkaMessagingService;

    private final ClientsService clientsService;

    private static int offset;

    private static final int BATCH_SIZE = 5;
    private static final int THREAD_COUNT = 8;


    private synchronized int getNextOffset() {
        int current = offset;
        offset += BATCH_SIZE;
        return current;
    }

    public void send() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(new Processor());
        }
        executor.shutdown();
    }

    private class Processor implements Runnable {
        @Override
        public void run() {
            int offset;
            while ((offset = getNextOffset()) < clientsService.getTotalRecords()) {
                List<ClientsAllInfo> clients = clientsService.clientsAllInfoList(offset);
                if(clients.isEmpty()) break;
                kafkaMessagingService.sendDataToKafka(clients);
            }
        }
    }

}
