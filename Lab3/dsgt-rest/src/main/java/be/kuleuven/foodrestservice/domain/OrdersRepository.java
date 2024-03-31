package be.kuleuven.foodrestservice.domain;

import be.kuleuven.foodrestservice.controllers.OrdersRestController;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class OrdersRepository {
    private final Map<String, Order> orders = new HashMap<>();

    public Order saveOrder(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    public Collection<Order> getAllOrders() {
        return orders.values();
    }

    public Order findById(String id) {
        Order order = orders.get(id);
        if (order == null) {
            throw new OrdersRestController.OrderNotFoundException(id);
        }
        return order;
    }

}



