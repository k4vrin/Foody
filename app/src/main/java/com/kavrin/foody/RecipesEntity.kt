package com.kavrin.foody

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kavrin.foody.models.FoodRecipe
import com.kavrin.foody.util.Constants.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe // Our table will contain only one row of food recipe
) {
    @PrimaryKey(autoGenerate = false) // Whenever we fetch a new data from our API, we are gonna replace all the data from our database table with new data
    var id: Int = 0
}