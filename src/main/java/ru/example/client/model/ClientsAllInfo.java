package ru.example.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientsAllInfo {
    private Long clientId;
    private String name;
    private Integer age;
    private String email;
    private String subscriptionType;
    private Integer price;
}
