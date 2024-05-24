package ru.example.client.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.example.client.mapper.ClientsAllInfoRowMapper;
import ru.example.client.model.ClientsAllInfo;
import ru.example.client.repository.exception.CustomDataAccessException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ClientsJDBCRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ClientsJDBCRepository clientsJDBCRepository;

    @Test
    public void thenReturnListOfClientsWhenQueryExecuted() {
        ClientsAllInfo client = ClientsAllInfo.builder()
                .clientId(1L)
                .name("Sophie Brown")
                .age(30)
                .email("sophie@example.com")
                .price(100)
                .subscriptionType("Basic").build();
        List<ClientsAllInfo> expectedList = List.of(client);
        Mockito.when(jdbcTemplate.query(anyString(), eq(new Object[]{0}), any(ClientsAllInfoRowMapper.class)))
                .thenReturn(expectedList);
        List<ClientsAllInfo> result = clientsJDBCRepository.clients(0);
        assertThat(result).isEqualTo(expectedList);
    }


    @Test
    public void thenReturnCorrectCountRecordsWhenCountQueryExecuted() {
        int expected = 10;
        String expectedSql = "select count(*)" +
                " from clients" +
                " inner join subscriptions on clients.client_id = subscriptions.client_id";
        Mockito.when(jdbcTemplate.queryForObject(expectedSql, Integer.class)).thenReturn(expected);
        int result = clientsJDBCRepository.getCountRecords();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void thenReturnEmptyListWhenNoClientsFromTable() {
        Mockito.when(jdbcTemplate.query(anyString(), eq(new Object[]{0}), any(ClientsAllInfoRowMapper.class)))
                .thenReturn(Collections.emptyList());
        List<ClientsAllInfo> result = clientsJDBCRepository.clients(0);
        assertThat(result).isEmpty();
    }

    @Test
    public void thenReturnZeroWhenNoClientsFromTable() {
        String expectedSql = "select count(*)" +
                " from clients" +
                " inner join subscriptions on clients.client_id = subscriptions.client_id";
        Mockito.when(jdbcTemplate.queryForObject(expectedSql, Integer.class)).thenReturn(0);
        int result = clientsJDBCRepository.getCountRecords();
        assertThat(result).isZero();
    }

    @Test
    public void thenTrowExceptionWhenCountRecordsQueryFails() {
        String expectedSql = "select count(*)" +
                " from clients" +
                " inner join subscriptions on clients.client_id = subscriptions.client_id";
        Mockito.when(jdbcTemplate.queryForObject(expectedSql, Integer.class))
                .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> clientsJDBCRepository.getCountRecords())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void thenThrowCustomException_whenClientQueryFails() {
        Mockito.when(jdbcTemplate.query(anyString(), any(Object[].class), any(ClientsAllInfoRowMapper.class)))
                .thenThrow(new DataAccessException("Query failed"){});
        assertThatThrownBy(() -> clientsJDBCRepository.clients(0))
                .isInstanceOf(CustomDataAccessException.class)
                .hasMessageContaining("Query failed");
    }

    @Test
    public void thenReturnEmptyListWhenCannotGetJdbcConnection() {
        Mockito.when(jdbcTemplate.query(anyString(), any(Object[].class), any(ClientsAllInfoRowMapper.class)))
                .thenThrow(new CannotGetJdbcConnectionException("Cannot get JDBC connection"));
        List<ClientsAllInfo> result = clientsJDBCRepository.clients(0);
        assertThat(result).isEmpty();
    }

    @Test
    public void thenReturnEmptyListWhenRuntimeException() {
        Mockito.when(jdbcTemplate.query(anyString(), any(Object[].class), any(ClientsAllInfoRowMapper.class)))
                .thenThrow(new RuntimeException());
        List<ClientsAllInfo> result = clientsJDBCRepository.clients(0);
        assertThat(result).isEmpty();
    }
}