package be.kuleuven.foodrestservice.domain;

import java.util.List;

public class Order {
    private String id; // Unique identifier for the order
    private String address;
    private List<String> mealIds; // Assuming you're working with meal IDs

    // No-arg constructor
    public Order() {
    }

    // Constructor with all fields
    public Order(String id, String address, List<String> mealIds) {
        this.id = id;
        this.address = address;
        this.mealIds = mealIds;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getMealIds() {
        return mealIds;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMealIds(List<String> mealIds) {
        this.mealIds = mealIds;
    }
}

