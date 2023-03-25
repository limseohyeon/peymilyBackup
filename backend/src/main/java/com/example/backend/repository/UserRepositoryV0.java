package com.example.backend.repository;

import com.example.backend.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
public class UserRepositoryV0 {
    private final JdbcTemplate template;
    public UserRepositoryV0(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }
}
