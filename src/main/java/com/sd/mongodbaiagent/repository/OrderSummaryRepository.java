package com.sd.mongodbaiagent.repository;

import com.sd.mongodbaiagent.model.OrderSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderSummaryRepository extends MongoRepository<OrderSummary, String> {
    @Query("{ 'vendorName': { $regex: ?0, $options: 'i' } }")
    List<OrderSummary> findByVendorNameIgnoreCase(String vendorName);

    @Query("{ 'lastOrderStatus': { $regex: ?0, $options: 'i' } }")
    List<OrderSummary> findByLastOrderStatusIgnoreCase(String status);

    List<OrderSummary> findByStoreId(String storeId);

    Optional<OrderSummary> findById(String id);
}
