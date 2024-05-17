package ru.example.client.repository;

import ru.example.client.model.ClientsAllInfo;

import java.util.List;

public interface ClientsRepository {
    List<ClientsAllInfo> clients(int offset);
    int getCountRecords();
}
