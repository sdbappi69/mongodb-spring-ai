package com.sd.mongodbaiagent.repository;

import com.sd.mongodbaiagent.model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository
        extends MongoRepository<Conversation, String> {
}

