package com.shoalter.cassandra.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
@Data
public class Users {

    @PrimaryKey
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private String email;

    public Users(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
