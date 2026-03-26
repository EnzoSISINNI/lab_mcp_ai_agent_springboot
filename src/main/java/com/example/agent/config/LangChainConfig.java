package com.example.agent.config;

import com.example.agent.agent.BacklogAgent;
import com.example.agent.tools.AgentTool;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class LangChainConfig {

    @Bean
    public AnthropicChatModel anthropicChatModel(
            @Value("${anthropic.api-key}") String apiKey,
            @Value("${anthropic.model}") String model,
            @Value("${anthropic.max-tokens:800}") Integer maxTokens,
            @Value("${anthropic.timeout-seconds:60}") Integer timeoutSeconds
    ) {
        return AnthropicChatModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .maxTokens(maxTokens)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }

    @Bean
    public BacklogAgent backlogAgent(AnthropicChatModel model,
                                     List<AgentTool> tools) {
        return AiServices.builder(BacklogAgent.class)
                .chatModel(model)
                .tools(tools.toArray())
                .build();
    }
}