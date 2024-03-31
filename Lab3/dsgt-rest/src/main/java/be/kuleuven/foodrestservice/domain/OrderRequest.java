package be.kuleuven.foodrestservice.domain;

import java.util.List;

public class OrderRequest {
    private String address;
    private List<String> mealIds; // Identifiers of the meals being ordered

    // No-arg constructor
    public OrderRequest() {
    }

    // Constructor with all fields
    public OrderRequest(String address, List<String> mealIds) {
        this.address = address;
        this.mealIds = mealIds;
    }

    // Getters
    public String getAddress() {
        return address;
    }

    public List<String> getMealIds() {
        return mealIds;
    }

    // Setters
    public void setAddress(String address) {
        this.address = address;
    }

    public void setMealIds(List<String> mealIds) {
        this.mealIds = mealIds;
    }
}

