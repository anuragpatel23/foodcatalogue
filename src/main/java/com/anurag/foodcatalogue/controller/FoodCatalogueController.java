package com.anurag.foodcatalogue.controller;

import com.anurag.foodcatalogue.dto.FoodCataloguePageDTO;
import com.anurag.foodcatalogue.dto.FoodItemDTO;
import com.anurag.foodcatalogue.service.FoodCatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foodCatalogue")
public class FoodCatalogueController {
    @Autowired
    FoodCatalogueService foodCatalogueService;

    @PostMapping("/addFoodItem")
    public ResponseEntity<FoodItemDTO> addFoodItem(@RequestBody FoodItemDTO foodItemDTO){
        FoodItemDTO foodSaved =foodCatalogueService.addFoodItemToDb(foodItemDTO);
        return new ResponseEntity<>(foodSaved, HttpStatus.CREATED);
    }

    @GetMapping("/fetchRestaurantAndFoodItemsById/{id}")
    public ResponseEntity<FoodCataloguePageDTO> fetchRestaurantAndFoodItemsById(@PathVariable Integer id){
        FoodCataloguePageDTO foodCataloguePage = foodCatalogueService.foodCataloguePageDetails(id);
        return new ResponseEntity<>(foodCataloguePage, HttpStatus.OK);
    }
}
