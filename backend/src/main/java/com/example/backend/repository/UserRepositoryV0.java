package com.example.backend.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
public class UserRepositoryV0 {
    private final JdbcTemplate template;
    public UserRepositoryV0(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }
}
