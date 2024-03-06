package com.example.springsoap;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


import io.foodmenu.gt.webservice.*;


import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MealRepository {
    private static final Map<String, Meal> meals = new HashMap<String, Meal>();

    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealtype(Mealtype.MEAT);
        a.setKcal(1100);
        a.setPrice(30);


        meals.put(a.getName(), a);

        Meal b = new Meal();
        b.setName("Portobello");
        b.setDescription("Portobello Mushroom Burger");
        b.setMealtype(Mealtype.VEGAN);
        b.setKcal(637);
        b.setPrice(15);


        meals.put(b.getName(), b);

        Meal c = new Meal();
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealtype(Mealtype.FISH);
        c.setKcal(950);
        c.setPrice(8);


        meals.put(c.getName(), c);

        Meal d = new Meal();
        d.setName("Grander");
        d.setDescription("Delicious burger with fried chicken");
        d.setMealtype(Mealtype.MEAT);
        d.setKcal(714);
        d.setPrice(12);

        meals.put(d.getName(), d);
    }

    public Meal findMeal(String name) {
        Assert.notNull(name, "The meal's code must not be null");
        return meals.get(name);
    }

    public Meal findBiggestMeal() {

        if (meals == null) return null;
        if (meals.size() == 0) return null;

        var values = meals.values();
        return values.stream().max(Comparator.comparing(Meal::getKcal)).orElseThrow(NoSuchElementException::new);

    }

    public Meal findCheapestMeal() {

        if (meals == null) return null;
        if (meals.size() == 0) return null;

        var values = meals.values();
        return values.stream().min(Comparator.comparing(Meal::getPrice)).orElseThrow(NoSuchElementException::new);

    }

    private static final Map<String, Confirmation> confirmations = new HashMap<String, Confirmation>();

    @PostConstruct
    public void initConfirmationData() {

        Confirmation a = new Confirmation();
        a.setName("Grander");
        a.setAddress("Leuven, Veldagigheitstrasse 73");

        confirmations.put(a.getName(), a);
    }

    public Confirmation orderMeal(String name, String address) {
        Assert.notNull(name, "The meal's name must not be null");
        Assert.notNull(address, "The delivery address must not be null");

        // Check if the meal exists
        Meal orderedMeal = meals.get(name);
        if (orderedMeal == null) {
            throw new NoSuchElementException("Meal not found: " + name);
        }

        // Create a new Confirmation object
        Confirmation confirmation = new Confirmation();
        confirmation.setName(name); // Set the meal name
        confirmation.setAddress(address); // Set the delivery address

        // Here you might be considering using the meal object directly,
        // but as you only need the name, we're setting the name directly.

        // Optionally generate a unique ID for the confirmation if needed

        // Store the confirmation
        confirmations.put(name, confirmation); // Consider using a unique identifier here

        return confirmation;
    }
}