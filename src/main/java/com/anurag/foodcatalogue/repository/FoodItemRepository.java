package com.anurag.foodcatalogue.repository;

import com.anurag.foodcatalogue.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Integer> {
    public List<FoodItem> findByRestaurantId(Integer restaurantId);
}
