package ru.example.client.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import ru.example.client.mapper.ClientsAllInfoRowMapper;
import ru.example.client.model.ClientsAllInfo;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class ClientsJDBCRepository implements ClientsRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    @Retryable(retryFor = {DataAccessException.class, CannotGetJdbcConnectionException.class},
            maxAttempts = 7, backoff = @Backoff(delay = 500))
    public List<ClientsAllInfo> clients(int offset) {
        String sql = "select clients.client_id,name,age,email,subscription_type,price" +
                " from clients" +
                " inner join subscriptions on clients.client_id = subscriptions.client_id" +
                " limit 5 offset ?";
        return jdbcTemplate.query(sql, new Object[]{offset}, new ClientsAllInfoRowMapper());
    }
}
