package ru.example.client.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.example.client.model.ClientsAllInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientsAllInfoRowMapper implements RowMapper<ClientsAllInfo> {
    @Override
    public ClientsAllInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ClientsAllInfo(
                rs.getLong("client_id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("email"),
                rs.getString("subscription_type"),
                rs.getInt("price")
        );
    }
}
