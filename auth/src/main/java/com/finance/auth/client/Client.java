package com.finance.auth.client;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor  // JPA가 반드시 필요로 함
@AllArgsConstructor // 우리가 직접 사용할 전체 생성자 (clientId, password, clientName)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL, PostgreSQL 기본 auto increment
    private Long id;

    @Column(nullable = false, unique = true)
    private String clientId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String clientName;

    public Client(String clientId, String password, String clientName) {
        this.clientId = clientId;
        this.password = password;
        this.clientName = clientName;
    }
}