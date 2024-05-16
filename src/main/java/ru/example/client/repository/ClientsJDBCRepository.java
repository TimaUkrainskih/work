package ru.example.client.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.example.client.repository.exception.CustomDataAccessException;
import ru.example.client.mapper.ClientsAllInfoRowMapper;
import ru.example.client.model.ClientsAllInfo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class ClientsJDBCRepository implements ClientsRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    @Transactional
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 500))
    public List<ClientsAllInfo> clients(int offset) {
        String sql = "select clients.client_id,name,age,email,subscription_type,price" +
                " from clients" +
                " inner join subscriptions on clients.client_id = subscriptions.client_id" +
                " order by clients.client_id" +
                " limit 5 offset ?";
        List<ClientsAllInfo> result = Collections.emptyList();
        try {
            result = jdbcTemplate.query(sql, new Object[]{offset}, new ClientsAllInfoRowMapper());
            update(offset);
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Cannot get JDBC connection: {}", e.getMessage(), e);
            Collections.emptyList();
        } catch (DataAccessException e) {
            log.error("Data access exception for query: {}{}", sql, System.lineSeparator() + e.getMessage(), e);
            throw new CustomDataAccessException(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected exception: {}", e.getMessage(), e);
            Collections.emptyList();
        }
        return result;
    }

    private void update(int offset) {
        jdbcTemplate.execute((Connection con) -> {
            try (CallableStatement callableStatement = con.prepareCall("call update_last_reading_dttm(?) ")) {
                callableStatement.setInt(1, offset);
                callableStatement.execute();
            }
            return null;
        });
    }
}
