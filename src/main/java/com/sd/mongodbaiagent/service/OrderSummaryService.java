package com.sd.mongodbaiagent.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sd.mongodbaiagent.model.OrderSummary;
import com.sd.mongodbaiagent.repository.OrderSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderSummaryService {

    @Autowired
    OrderSummaryRepository summaryRepository;

    @Autowired
    ObjectMapper objectMapper;

    public String getOrdersByVendorName(String vendorName) throws JsonProcessingException {
        List<OrderSummary> orders = summaryRepository.findByVendorNameIgnoreCase(vendorName);
        System.out.println(orders.get(0).getId());
        String ordersJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orders);
        return ordersJson;
    }

    public List<OrderSummary> getOrdersByStatus(String status) {
        if (status == null) return List.of();
        List<OrderSummary> orders = summaryRepository.findByLastOrderStatusIgnoreCase(status);
        return (orders == null || orders.isEmpty()) ? List.of() : orders;
    }

    public List<OrderSummary> getOrdersByStore(String storeId) {
        if (storeId == null) return List.of();
        List<OrderSummary> orders =  summaryRepository.findByStoreId(storeId);
        return (orders == null || orders.isEmpty()) ? List.of() : orders;
    }

    public Optional<OrderSummary> getOrderById(String storeId) {
        if (storeId == null) return null;
        return summaryRepository.findById(storeId);
    }

}
