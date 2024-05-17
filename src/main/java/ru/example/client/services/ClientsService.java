package ru.example.client.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.client.model.ClientsAllInfo;
import ru.example.client.repository.ClientsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientsService {

    private final ClientsRepository clientsRepository;

    public List<ClientsAllInfo> clientsAllInfoList(int offset) {
        return clientsRepository.clients(offset);
    }

    public int getTotalRecords() {
        return clientsRepository.getCountRecords();
    }
}
