package com.sd.mongodbaiagent.service;

import com.sd.mongodbaiagent.mcp.tool.OrderSummaryTool;
import com.sd.mongodbaiagent.model.Conversation;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    private final ChatClient chatClient;
    private final ConversationService conversationService;

    private static final String SYSTEM_PROMPT = """
        You are an AI assistant for a retail Purchase Order Vendor Adapter system.
        
        You can:
        - Answer questions about orders
        - Explain order failures
        - Summarize vendor errors
        - Compare ordered vs shipped quantities
        - Identify stuck or failed orders
        
        Rules:
        - Always base answers on MongoDB query results
        - If data is missing, clearly say so
        - Use concise, operational language
        - Do not hallucinate order data
        
        OrderSummary fields:
        - id: Order ID
        - submittedDateTime: ISO timestamp when order was submitted
        - lastUpdateDatetime: ISO timestamp of last update
        - vendorName: name of the vendor
        - storeId: retail store ID
        - authorizedRetailerId: retailer authorization ID
        - shippingMethod: shipping method (Ground, Priority, etc.)
        - lastOrderStatus: status of the order (ERROR, SUCCESS, PENDING)
        - lastVendorStatus: vendor system status
        - lastEvent: last workflow event (CREATE_PURCHASE_ORDER, etc.)
        - vendorAccountId: internal vendor account ID
        - vendorOrderId: vendor system order ID
        - orderedSkus: map of SKU to quantity ordered
        - shippedSkus: map of SKU to quantity shipped
        - message: internal messages
        - error: error messages, if any
        - vendorError: vendor system errors
        - clusterName: Kubernetes cluster name
        - podName: pod name
        """;

    public AgentService(
            ChatClient.Builder builder,
            OrderSummaryTool mongoTool,
            ConversationService conversationService
    ) {
        this.chatClient = builder
                .defaultTools(mongoTool)
                .build();
        this.conversationService = conversationService;
    }

    public String ask(String conversationId, String userPrompt) {

        // 1️⃣ Load or create conversation
        Conversation conversation =
                conversationService.getOrCreate(conversationId);

        // 2️⃣ Add user message to memory
        conversationService.addUserMessage(conversation, userPrompt);

        // 3️⃣ Convert stored messages to Spring AI messages
        List<Message> messages = conversation.getMessages().stream()
                .map(m -> (Message) switch (m.getRole()) {
                    case "user" -> new UserMessage(m.getContent());
                    case "assistant" -> new AssistantMessage(m.getContent());
                    default -> throw new IllegalStateException("Unknown role: " + m.getRole());
                })
                .toList();

        // 4️⃣ Call AI with FULL HISTORY
        String answer = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(messages)
                .call()
                .content();

        // 5️⃣ Store assistant reply
        conversationService.addAssistantMessage(conversation, answer);

        return answer;
    }
}

