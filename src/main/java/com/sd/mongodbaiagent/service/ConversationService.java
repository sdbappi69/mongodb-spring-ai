package com.sd.mongodbaiagent.service;

import com.sd.mongodbaiagent.model.Conversation;
import com.sd.mongodbaiagent.model.Message;
import com.sd.mongodbaiagent.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private static final int MAX_MESSAGES = 10;

    private final ConversationRepository conversationRepository;

    public Conversation getOrCreate(String conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseGet(() -> {
                    Conversation conversation = new Conversation();
                    conversation.setId(conversationId);
                    return conversationRepository.save(conversation);
                });
    }

    public void addUserMessage(Conversation conversation, String text) {
        conversation.getMessages().add(new Message("user", text));
        trim(conversation);
        conversationRepository.save(conversation);
    }

    public void addAssistantMessage(Conversation conversation, String text) {
        conversation.getMessages().add(new Message("assistant", text));
        trim(conversation);
        conversationRepository.save(conversation);
    }

    /**
     * Ensures conversation memory never exceeds MAX_MESSAGES.
     * Oldest messages are removed first.
     */
    private void trim(Conversation conversation) {
        var messages = conversation.getMessages();

        if (messages.size() > MAX_MESSAGES) {
            int excess = messages.size() - MAX_MESSAGES;
            messages.subList(0, excess).clear();
        }
    }
}

