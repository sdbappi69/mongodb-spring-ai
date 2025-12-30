package com.sd.mongodbaiagent.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("conversations")
public class Conversation {

    @Id
    private String id;

    private List<Message> messages = new ArrayList<>();
}

