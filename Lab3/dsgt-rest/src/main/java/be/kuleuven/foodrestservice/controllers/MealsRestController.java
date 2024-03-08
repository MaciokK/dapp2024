package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    /*@GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> addMeal() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).addMeal()).withSelfRel());
    }

    @PostMapping("/rest/meals")
    ResponseEntity<Meal> createMeal(@RequestBody Meal meal) {
        // Logic to add meal to repository
        // Return 201 Created status and include the meal in the body, typically with a Location header as well
    }

    @PutMapping("/rest/meals/{id}")
    ResponseEntity<Meal> updateMeal(@PathVariable String id, @RequestBody Meal meal) {
        // Logic to update the meal in the repository
        // Return 200 OK status and include the updated meal in the body
    }

    @DeleteMapping("/rest/meals/{id}")
    ResponseEntity<?> deleteMeal(@PathVariable String id) {
        // Logic to remove the meal from the repository
        // Return 204 No Content status if successful
    }*/

    private EntityModel<Meal> mealToEntityModel(String id, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(id)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("rest/meals"));
                //linkTo(methodOn(MealsRestController.class).getCheapestMeal()).withRel("rest/meals/cheapest"),
                //linkTo(methodOn(MealsRestController.class).getBiggestMeal()).withRel("rest/meals/biggest"));
    }

}
