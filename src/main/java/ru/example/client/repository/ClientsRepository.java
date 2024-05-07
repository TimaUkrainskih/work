package ru.example.client.repository;

import java.util.List;
import java.util.Map;

public interface ClientsRepository {
    public List<Map<String, Object>> clients();
}
