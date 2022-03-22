package com.shoalter.cassandra.controller;

import com.shoalter.cassandra.dao.UsersDao;
import com.shoalter.cassandra.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DockerController {

    private Logger logger = LoggerFactory.getLogger(DockerController.class);

    @Autowired
    private UsersDao usersDao;

    @RequestMapping("/hi")
    public String index() {
        return "Hello Docker!";
    }

    @GetMapping("/getUser")
    public ResponseEntity<Users> getUser(@RequestParam int id) {
        try {
            Users users = usersDao.findById(id).orElseThrow();
            logger.info("[cassandra] - Query success!");
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/addUser")
    public ResponseEntity<Users> addUser(@RequestParam int id, @RequestParam String name, @RequestParam String email) {
        try {
            Users users = usersDao.save(new Users(id, name, email));
            logger.info("[cassandra] - Create success!");
            return new ResponseEntity<>(users, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<Users>> getAllUser() {
        try {
            List<Users> usersList = usersDao.findAll();
            logger.info("[cassandra] - Query success!");
            return new ResponseEntity<>(usersList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
