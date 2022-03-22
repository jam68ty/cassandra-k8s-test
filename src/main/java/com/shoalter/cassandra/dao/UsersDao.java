package com.shoalter.cassandra.dao;

import com.shoalter.cassandra.entity.Users;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersDao extends CassandraRepository<Users, Integer> {
    @AllowFiltering
    List<Users> findByEmail(String email);

}
