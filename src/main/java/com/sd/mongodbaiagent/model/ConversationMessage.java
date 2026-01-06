package com.sd.mongodbaiagent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationMessage {
    private String role;    // "user" | "assistant"
    private String content;
}

