package com.example.agent.web;

import com.example.agent.agent.BacklogAgent;
import com.example.agent.mcp.McpHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AgentControllerIT {

    @Autowired
    TestRestTemplate rest;

    @MockBean
    McpHttpClient mcp;

    @MockBean
    BacklogAgent backlogAgent;

    @Test
    void should_call_endpoint() {
        // Arrange
        when(mcp.callTool(eq("create_issue"), anyMap()))
                .thenReturn(Mono.just(Map.of(
                        "number", 1,
                        "html_url", "https://github.com/o/r/issues/1"
                )));

        when(backlogAgent.handle(anyString()))
                .thenReturn("Issue created successfully");

        // Act
        ResponseEntity<String> response = rest.postForEntity(
                "/api/run",
                "Create a task to add OpenTelemetry",
                String.class
        );

        // Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("Issue created successfully");
    }
}