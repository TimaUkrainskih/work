package ru.example.client.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ClientsJDBCRepository implements ClientsRepository{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> clients() {
        return jdbcTemplate.queryForList("select * from clients inner join subscriptions on clients.client_id = subscriptions.client_id");
    }
}
