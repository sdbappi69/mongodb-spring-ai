package com.sd.mongodbaiagent.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Document(collection = "order_summary")
public class OrderSummary {
    @Id
    private String id;

    private Instant submittedDateTime;
    private Instant lastUpdateDatetime;

    private String vendorName;
    private String storeId;
    private String authorizedRetailerId;
    private String shippingMethod;

    private String lastOrderStatus;
    private String lastVendorStatus;
    private String lastEvent;

    private String vendorAccountId;
    private String vendorOrderId;

    private Map<String, SkuQuantity> orderedSkus;
    private Map<String, SkuQuantity> shippedSkus;

    private String message;
    private String error;
    private String vendorError;

    private String clusterName;
    private String podName;
}
