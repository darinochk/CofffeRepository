package org.example.coffeservice.services;

import org.example.coffeservice.models.OrderDetails;
import org.example.coffeservice.repositories.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

//    public List<OrderDetails> getOrderDetailsByOrderId(Long orderId) {
//        return orderDetailsRepository.findByOrderId(orderId);
//    }
}
