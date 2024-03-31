package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.domain.OrderRequest;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrdersRestController {

    private final OrdersRepository ordersRepository;

    @Autowired
    public OrdersRestController(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Autowired
    private MealsRepository mealsRepository; // Add this to your OrdersRestController


    @GetMapping("/rest/orders")
    public CollectionModel<EntityModel<Order>> getAllOrders() {
        List<EntityModel<Order>> orders = ordersRepository.getAllOrders().stream()
                .map(order -> EntityModel.of(order,
                        linkTo(methodOn(OrdersRestController.class).getOrderById(order.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(orders, linkTo(methodOn(OrdersRestController.class).getAllOrders()).withSelfRel());
    }

    @PostMapping("/rest/orders")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest) {
        // Validate the meal IDs
        List<Meal> orderedMeals = orderRequest.getMealIds().stream()
                .map(mealsRepository::findMeal)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (orderedMeals.size() != orderRequest.getMealIds().size()) {
            return ResponseEntity.badRequest().body("One or more meals could not be found.");
        }

        // Create and save the order
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setAddress(orderRequest.getAddress());
        order.setMealIds(orderRequest.getMealIds()); // Assuming you're storing just the IDs

        ordersRepository.saveOrder(order);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(order.getId()).toUri();

        return ResponseEntity.created(location).body(order);
    }

    @GetMapping("/rest/orders/{id}")
    public EntityModel<Order> getOrderById(@PathVariable String id) {
        Order order = ordersRepository.findById(id);

        return EntityModel.of(order,
                linkTo(methodOn(OrdersRestController.class).getOrderById(id)).withSelfRel(),
                linkTo(methodOn(OrdersRestController.class).getAllOrders()).withRel("orders"));
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND) // This makes Spring automatically return a 404 Not Found HTTP status code when this exception is thrown
    public static class OrderNotFoundException extends RuntimeException {

        public OrderNotFoundException(String id) {
            super("Could not find order " + id);
        }
    }
}
