package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.domain.OrderRequest;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class MealsRestController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/rest/meals/{id}")
    EntityModel<Meal> getMealById(@PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id).orElseThrow(() -> new MealNotFoundException(id));

        return mealToEntityModel(id, meal);
    }

    @GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> getMeals() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getCheapestMeal()).withRel("rest/meals/cheapest"),
                linkTo(methodOn(MealsRestController.class).getBiggestMeal()).withRel("rest/meals/biggest"));
    }

    @GetMapping("/rest/meals/cheapest")
    EntityModel<Meal> getCheapestMeal() {
        Meal cheapestMeal = mealsRepository.findCheapestMeal()
                .orElseThrow(() -> new MealNotFoundException("cheapest"));

        return EntityModel.of(cheapestMeal,
                linkTo(methodOn(MealsRestController.class).getCheapestMeal()).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("meals"));
    }

    @GetMapping("/rest/meals/biggest")
    EntityModel<Meal> getBiggestMeal() {
        Meal biggestMeal = mealsRepository.findBiggestMeal()
                .orElseThrow(() -> new MealNotFoundException("biggest"));

        return EntityModel.of(biggestMeal,
                linkTo(methodOn(MealsRestController.class).getBiggestMeal()).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("meals"));
    }

    @PostMapping("/rest/meals")
    public ResponseEntity<?> createMeal(@RequestBody Meal newMeal) {
        // Generate a new UUID for the meal and set it
        newMeal.setId(UUID.randomUUID().toString());

        // Save the new meal in the repository
        mealsRepository.addMeal(newMeal);

        // Build the location of the newly created meal
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newMeal.getId())
                .toUri();

        // Return the response entity with the 201 status code and the location header
        return ResponseEntity.created(location).body(newMeal);
    }

    @PutMapping("/rest/meals/{id}")
    public ResponseEntity<?> updateMeal(@PathVariable String id, @RequestBody Meal mealUpdate) {
        Meal existingMeal = mealsRepository.findMeal(id)
                .orElseThrow(() -> new MealNotFoundException(id));

        // Update the existing meal's properties with the ones from mealUpdate
        existingMeal.setName(mealUpdate.getName());
        existingMeal.setDescription(mealUpdate.getDescription());
        existingMeal.setMealType(mealUpdate.getMealType());
        existingMeal.setKcal(mealUpdate.getKcal());
        existingMeal.setPrice(mealUpdate.getPrice());

        // Assume you have a method in MealsRepository to save the updated meal
        mealsRepository.saveMeal(existingMeal);

        // Build the location URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(existingMeal.getId())
                .toUri();

        // Return the response entity with 200 OK status and the updated meal
        return ResponseEntity.ok()
                .location(location)
                .body(existingMeal);
    }

    @DeleteMapping("/rest/meals/{id}")
    public ResponseEntity<?> deleteMeal(@PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id)
                .orElseThrow(() -> new MealNotFoundException(id));

        // Remove the meal from the repository
        mealsRepository.deleteMeal(id);

        // Return a 204 No Content response to indicate the deletion was successful
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Meal> mealToEntityModel(String id, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(id)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("rest/meals"));
                //linkTo(methodOn(MealsRestController.class).getCheapestMeal()).withRel("rest/meals/cheapest"),
                //linkTo(methodOn(MealsRestController.class).getBiggestMeal()).withRel("rest/meals/biggest"));
    }

}



