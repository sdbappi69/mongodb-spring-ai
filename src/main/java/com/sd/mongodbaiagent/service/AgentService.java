package com.sd.mongodbaiagent.service;

import com.sd.mongodbaiagent.mcp.tool.OrderSummaryTool;
import com.sd.mongodbaiagent.model.Conversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    private static final Logger log =
            LoggerFactory.getLogger(AgentService.class);
    private final ChatClient chatClient;
    private final ConversationService conversationService;

    private static final String SYSTEM_PROMPT = """
        You are an AI agent for a gateway service named POVS.
        POVS stands for Purchase Order Vendor Service.
        POVS is a single system name and must NEVER be paraphrased, expanded, or split
        into phrases like "purchase orders or vendor services".
               
        Scope:
        - POVS handles purchase order orchestration between retail systems and vendors.
        - All answers must strictly relate to POVS data and MongoDB query results.
               
        Capabilities:
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
        
        Greeting Behavior:
        - If the user greets (e.g., "hi", "hello"):
          - Respond naturally and briefly
          - Mention POVS Agent as a system name
          - Invite the user to ask about order data or system state
          - Do NOT describe or expand POVS
          - Do NOT use customer-support or sales language
        
        OrderSummary fields:
        - id: Order ID
        - submittedDateTime: ISO timestamp when order was submitted
        - lastUpdateDatetime: ISO timestamp of last update
        - vendorName: name of the vendor
        - storeId: retail store ID
        - authorizedRetailerId: retailer authorization ID
        - shippingMethod: shipping method (Ground, Priority, etc.)
        - lastOrderStatus: order status (ERROR, CREATED, ORDER_APPROVED, etc.)
            - Only ERROR represents a failed order
            - All other statuses are considered successful
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
                    .system(SYSTEM_PROMPT)
                    .messages(messages)
                    .call()
                    .content();

            conversationService.addAssistantMessage(conversation, answer);

            return answer;

        } catch (Exception ex) {
            log.error("AI processing failed for conversationId={}", conversationId, ex);
            return "Sorry, I ran into an internal error while processing your request. Please try again.";
        }
    }

}

