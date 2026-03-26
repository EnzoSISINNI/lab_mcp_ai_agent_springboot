package com.example.agent.web;

import com.example.agent.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT {

    @Autowired
    TestRestTemplate rest;

    @Test
    void should_create_and_get_user() {
        // 1) Create a user
        User created = rest.postForObject(
                "/api/users?name=Alice&email=alice@example.com",
                null,
                User.class
        );

        assertThat(created).isNotNull();
        assertThat(created.id()).isNotBlank();
        assertThat(created.name()).isEqualTo("Alice");
        assertThat(created.email()).isEqualTo("alice@example.com");

        // 2) Retrieve the user by id
        User fetched = rest.getForObject(
                "/api/users/" + created.id(),
                User.class
        );

        assertThat(fetched).isNotNull();
        assertThat(fetched.id()).isEqualTo(created.id());
        assertThat(fetched.name()).isEqualTo("Alice");
        assertThat(fetched.email()).isEqualTo("alice@example.com");
    }
}