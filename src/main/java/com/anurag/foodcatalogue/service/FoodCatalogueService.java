package com.anurag.foodcatalogue.service;

import com.anurag.foodcatalogue.dto.FoodCataloguePageDTO;
import com.anurag.foodcatalogue.dto.FoodItemDTO;
import com.anurag.foodcatalogue.dto.RestaurantDTO;
import com.anurag.foodcatalogue.entity.FoodItem;
import com.anurag.foodcatalogue.mapper.FoodItemMapper;
import com.anurag.foodcatalogue.repository.FoodItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FoodCatalogueService {

    @Autowired
    FoodItemRepository foodItemRepository;

    @Autowired
    RestTemplate restTemplate;

    public FoodItemDTO addFoodItemToDb(FoodItemDTO foodItemDTO){
        FoodItem foodAdded = foodItemRepository.save(FoodItemMapper.INSTANCE.mapFoodItemDTOToFoodItem(foodItemDTO));
        return FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDTO(foodAdded);
    }

    @CircuitBreaker(name = "restaurantMSBreaker", fallbackMethod = "restaurantMSFallback")
    public FoodCataloguePageDTO foodCataloguePageDetails(Integer restaurantId){
        //fetch food Item from this service
        List<FoodItem> foodItemList = fetchFoodItemList(restaurantId);

        //fetch restaurant details from Restaurant Microservice
        RestaurantDTO restaurant = fetchRestaurantDetailsFromRestaurantMS(restaurantId);

        //club both responses for Food Catalogue page
        return createFoodCataloguePage(foodItemList, restaurant);

    }

    private List<FoodItem> fetchFoodItemList(Integer restaurantId) {
        return foodItemRepository.findByRestaurantId(restaurantId);
    }

    public RestaurantDTO fetchRestaurantDetailsFromRestaurantMS(Integer restaurantId) {
        try {
            return restTemplate.getForObject("http://RESTAURANT-SERVICE/restaurant/fetchById/" + restaurantId, RestaurantDTO.class);
        }catch(RestClientException ex){
            System.err.println("RestClientException caught: " + ex.getMessage());
            throw new RuntimeException("Failed to fetch restaurant details", ex);
        }
    }

    public FoodCataloguePageDTO restaurantMSFallback(Integer restaurantId, Throwable throwable){
        FoodCataloguePageDTO foodCataloguePageDTO = new FoodCataloguePageDTO();
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setId(1);
        restaurantDTO.setCity("not found");
        restaurantDTO.setName("failure");
        restaurantDTO.setAddress("404 - not found!!!");
        foodCataloguePageDTO.setRestaurant(restaurantDTO);

        return foodCataloguePageDTO;
    }

    private FoodCataloguePageDTO createFoodCataloguePage(List<FoodItem> foodItemList, RestaurantDTO restaurant) {
        FoodCataloguePageDTO foodCataloguePage = new FoodCataloguePageDTO();
        foodCataloguePage.setFoodItemList(foodItemList);
        foodCataloguePage.setRestaurant(restaurant);
        return foodCataloguePage;
    }
}
