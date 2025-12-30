package com.sd.mongodbaiagent.mcp.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sd.mongodbaiagent.model.OrderSummary;
import com.sd.mongodbaiagent.service.OrderSummaryService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderSummaryTool {

    @Autowired
    OrderSummaryService orderSummaryService;

    @Tool(description = "Find orders by vendor name")
    public String findOrdersByVendor(String vendorName) throws JsonProcessingException {
        return orderSummaryService.getOrdersByVendorName(vendorName);
    }

    @Tool(description = "Find orders by last order status like ERROR or SUCCESS")
    public List<OrderSummary> findOrdersByStatus(String status) {
        return orderSummaryService.getOrdersByStatus(status);
    }

    @Tool(description = "Find orders by store ID")
    public List<OrderSummary> findOrdersByStore(String storeId) {
        return orderSummaryService.getOrdersByStore(storeId);
    }

    @Tool(description = "Find order by ID")
    public Optional<OrderSummary> findOrderById(String storeId) {
        return orderSummaryService.getOrderById(storeId);
    }
}