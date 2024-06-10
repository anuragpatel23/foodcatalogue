package com.anurag.foodcatalogue.dto;

import com.anurag.foodcatalogue.entity.FoodItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodCataloguePageDTO {

    private List<FoodItem> foodItemList;
    private RestaurantDTO restaurant;
}
