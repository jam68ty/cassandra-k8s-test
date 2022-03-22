package com.shoalter.cassandra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.shoalter.cassandra.dao.*")
public class CassandraApplication {
    public static void main(String[] args) {
        SpringApplication.run(CassandraApplication.class, args);
    }
}
