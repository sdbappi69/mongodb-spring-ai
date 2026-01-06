package com.sd.mongodbaiagent.controller;

import com.sd.mongodbaiagent.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestHeader("X-Conversation-Id") String conversationId,
            @RequestBody String prompt
    ) {
        return ResponseEntity.ok(
                agentService.ask(conversationId, prompt)
        );
    }

}


