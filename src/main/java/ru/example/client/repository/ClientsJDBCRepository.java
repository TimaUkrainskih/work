package ru.example.client.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.example.client.mapper.ClientsAllInfoRowMapper;
import ru.example.client.model.ClientsAllInfo;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class ClientsJDBCRepository implements ClientsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ClientsAllInfo> clients(int offset) {
        String sql = "select clients.client_id,name,age,email,subscription_type,price" +
                " from clients" +
                " inner join subscriptions on clients.client_id = subscriptions.client_id" +
                " limit 5 offset ?";
        return jdbcTemplate.query(sql, new Object[]{offset}, new ClientsAllInfoRowMapper());
    }
}
