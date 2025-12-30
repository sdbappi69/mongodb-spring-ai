package com.sd.mongodbaiagent.service;

import com.sd.mongodbaiagent.model.Conversation;
import com.sd.mongodbaiagent.model.Message;
import com.sd.mongodbaiagent.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationService {

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
        conversationRepository.save(conversation);
    }

    public void addAssistantMessage(Conversation conversation, String text) {
        conversation.getMessages().add(new Message("assistant", text));
        conversationRepository.save(conversation);
    }
}

