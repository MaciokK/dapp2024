package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @GetMapping("/restrpc/meals/cheapest")
    Optional<Meal> getCheapestMeal() {
        return mealsRepository.findCheapestMeal();
    }

    @GetMapping("/restrpc/meals/biggest")
    Optional<Meal> getBiggestMeal() {
        return mealsRepository.findBiggestMeal();
    }

    @PostMapping("/restrpc/meals")
    ResponseEntity<?> createMeal(@RequestBody Meal newMeal){
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
    @PutMapping("/restrpc/meals/{id}")
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

    @DeleteMapping("/restrpc/meals/{id}")
    public ResponseEntity<?> deleteMeal(@PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id)
                .orElseThrow(() -> new MealNotFoundException(id));

        // Remove the meal from the repository
        mealsRepository.deleteMeal(id);

        // Return a 204 No Content response to indicate the deletion was successful
        return ResponseEntity.noContent().build();
    }
}
