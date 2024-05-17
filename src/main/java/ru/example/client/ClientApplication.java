package ru.example.client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import ru.example.client.kafka.ClientDataKafkaSenderMultiThreading;

@RequiredArgsConstructor
@EnableRetry
@SpringBootApplication
public class ClientApplication implements ApplicationRunner {

	private final ClientDataKafkaSenderMultiThreading kafkaSender;

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		kafkaSender.send();
	}
}
