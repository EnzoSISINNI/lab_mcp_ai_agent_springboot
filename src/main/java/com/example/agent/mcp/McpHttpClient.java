package com.example.agent.mcp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Component
public class McpHttpClient {

    private final WebClient web;
    private final String path;

    public McpHttpClient(WebClient.Builder builder,
                         @Value("${mcp.base-url}") String baseUrl,
                         @Value("${mcp.path:/mcp}") String path) {
        this.web = builder.baseUrl(baseUrl).build();
        this.path = path;
    }

    public Mono<Object> callTool(String toolName, Map<String, Object> arguments) {
        Map<String, Object> payload = Map.of(
                "jsonrpc", "2.0",
                "id", UUID.randomUUID().toString(),
                "method", "tools/call",
                "params", Map.of(
                        "name", toolName,
                        "arguments", arguments
                )
        );

        return web.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .onStatus(s -> s.isError(), r ->
                        r.bodyToMono(String.class)
                                .map(b -> new RuntimeException("MCP HTTP " + r.statusCode() + ": " + b)))
                .bodyToMono(Map.class)
                .map(resp -> {
                    if (resp.containsKey("error")) {
                        throw new RuntimeException("MCP error full response: " + resp);
                    }
                    Object result = resp.get("result");
                    if (result == null) {
                        throw new RuntimeException("MCP missing result, full response: " + resp);
                    }
                    return (Map) result;
                });
    }

    public Mono<Object> listTools() {
        Map<String, Object> payload = Map.of(
                "jsonrpc", "2.0",
                "id", UUID.randomUUID().toString(),
                "method", "tools/list",
                "params", Map.of()
        );

        return web.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .onStatus(s -> s.isError(), r ->
                        r.bodyToMono(String.class)
                                .map(b -> new RuntimeException("MCP HTTP " + r.statusCode() + ": " + b)))
                .bodyToMono(Map.class)
                .map(resp -> {
                    if (resp.containsKey("error")) {
                        throw new RuntimeException("MCP error: " + resp.get("error"));
                    }
                    return resp.get("result");
                });
    }
}