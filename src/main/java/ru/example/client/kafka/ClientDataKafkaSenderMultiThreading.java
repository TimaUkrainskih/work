package ru.example.client.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.client.model.ClientsAllInfo;
import ru.example.client.services.ClientsService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RequiredArgsConstructor
@Service
@Slf4j
public class ClientDataKafkaSenderMultiThreading {

    private final KafkaMessagingService kafkaMessagingService;

    private final ClientsService clientsService;

    private static final int THREAD_COUNT = 8;

    public void send() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Processor> taskList = new ArrayList<>();
        int totalRecord = clientsService.getTotalRecords();
        for (int i = 0; i < totalRecord; i += 5) {
            Processor task = new Processor(i);
            taskList.add(task);
        }
        try {
            executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();
    }

    private class Processor implements Callable<String> {

        private final int offset;

        public Processor(int offset) {
            this.offset = offset;
        }

        @Override
        public String call() {
            log.info(Thread.currentThread().getName());
            List<ClientsAllInfo> clients = clientsService.clientsAllInfoList(offset);
            kafkaMessagingService.sendDataToKafka(clients);
            return "Done";
        }
    }

}
