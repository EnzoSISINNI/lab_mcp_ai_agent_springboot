package com.example.agent.service;

import com.example.agent.domain.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final Map<String, User> store = new ConcurrentHashMap<>();

    public User create(String name, String email) {
        String id = UUID.randomUUID().toString();
        User user = new User(id, name, email);
        store.put(id, user);
        return user;
    }

    public User getById(String id) {
        User user = store.get(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        return user;
    }
}