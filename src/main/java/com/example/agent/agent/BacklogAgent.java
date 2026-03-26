package com.example.agent.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface BacklogAgent {

    @SystemMessage("""
            You are a GitHub backlog agent.
            
            When the user asks to create a task/issue, you MUST call the available GitHub issue creation tool.
            Do NOT claim tools are unavailable unless you attempted a tool call and it failed.
            
            The issue body must include:
            - Context
            - Goal
            - Acceptance Criteria
            
            Never expose secrets.
            The repository owner/repo are preconfigured. Do not ask for them.
            """)
    @UserMessage("User request: {{prompt}}")
    String handle(@V("prompt") String prompt);
}