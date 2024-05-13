package ru.example.client.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import ru.example.client.exception.CustomDataAccessException;
import ru.example.client.mapper.ClientsAllInfoRowMapper;
import ru.example.client.model.ClientsAllInfo;

import java.util.Collections;
import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class ClientsJDBCRepository implements ClientsRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 500))
    public List<ClientsAllInfo> clients(int offset) {
        String sql = "select client.client_id,name,age,email,subscription_type,price" +
                " from clients" +
                " inner join subscriptions on clients.client_id = subscriptions.client_id" +
                " limit 5 offset ?";
        return jdbcTemplate.query(sql, new Object[]{offset}, new ClientsAllInfoRowMapper());
    }

    @Recover
    public List<ClientsAllInfo> recoverDataAccess(DataAccessException e) {
        log.error("recoverDataAccess " + e.getMessage());
        throw new CustomDataAccessException("Error");
    }

    @Recover
    public List<ClientsAllInfo> recoverCannotGetJdbcConnection(CannotGetJdbcConnectionException e) {
        log.error("recoverCannotGetJdbcConnection " + e.getMessage());
        return Collections.emptyList();
    }

    @Recover
    public List<ClientsAllInfo> recoverException(Exception e) {
        log.error("recoverException " + e.getMessage());
        return Collections.emptyList();
    }

}
