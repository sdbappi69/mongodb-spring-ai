package com.sd.mongodbaiagent.service;

import com.sd.mongodbaiagent.mcp.tool.OrderSummaryTool;
import com.sd.mongodbaiagent.model.Conversation;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class AgentService {
    private static final Logger log =
            LoggerFactory.getLogger(AgentService.class);
    private final ChatClient chatClient;
    private final ConversationService conversationService;
    private String systemPrompt;
    @Value("${spring.data.ai.system-prompt-file-url:prompts/system-prompt.txt}")
    private String systemPromptFileUrl;
    @Value("${spring.data.ai.error-message:Sorry I ran into an internal error while processing your request. Please try again.}")
    private String errorMessage;

    public AgentService(ChatClient.Builder builder, OrderSummaryTool mongoTool, ConversationService conversationService) {
        this.chatClient = builder.defaultTools(mongoTool).build();
        this.conversationService = conversationService;
    }

    @PostConstruct
    public void init() {
        try {
            Path path = new ClassPathResource(systemPromptFileUrl).getFile().toPath();
            this.systemPrompt = Files.readString(path);
        } catch (Exception ex) {
            log.error("Failed to load system prompt from file: {}", systemPromptFileUrl, ex);
            throw new IllegalStateException("Could not initialize AgentService due to system prompt loading failure", ex);
        }
    }

    public String ask(String conversationId, String userPrompt) {

        Conversation conversation =
                conversationService.getOrCreate(conversationId);

        conversationService.addUserMessage(conversation, userPrompt);

        List<Message> messages = conversation.getConversationMessages().stream()
                .map(m -> (Message) switch (m.getRole()) {
                    case "user" -> new UserMessage(m.getContent());
                    case "assistant" -> new AssistantMessage(m.getContent());
                    default -> throw new IllegalStateException("Unknown role: " + m.getRole());
                })
                .toList();

        try {
            String answer = chatClient.prompt()
                    .system(systemPrompt)
                    .messages(messages)
                    .call()
                    .content();

            conversationService.addAssistantMessage(conversation, answer);

            return answer;

        } catch (Exception ex) {
            log.error("AI processing failed for conversationId={}", conversationId, ex);
            return errorMessage;
        }
    }

}

