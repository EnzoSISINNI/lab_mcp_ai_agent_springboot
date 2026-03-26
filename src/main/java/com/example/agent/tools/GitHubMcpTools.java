package com.example.agent.tools;

import com.example.agent.mcp.McpHttpClient;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GitHubMcpTools implements AgentTool {

    private final McpHttpClient mcp;
    private final String owner;
    private final String repo;

    public GitHubMcpTools(
            McpHttpClient mcp,
            @Value("${github.owner}") String owner,
            @Value("${github.repo}") String repo
    ) {
        this.mcp = mcp;
        this.owner = owner;
        this.repo = repo;
    }

    @Tool("Create a GitHub issue in the configured repository. Use when the user asks to create a task/issue.")
    public String createIssue(
            @P("Issue title") String title,
            @P("Issue body in Markdown") String body
    ) {
        Map result = (Map) mcp.callTool("create_issue", Map.of(
                "owner", owner,
                "repo", repo,
                "title", title,
                "body", body
        )).block();

        return "Issue created successfully: " + result;
    }
}