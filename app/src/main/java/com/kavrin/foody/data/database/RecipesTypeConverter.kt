package com.kavrin.foody.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kavrin.foody.models.FoodRecipe
import com.kavrin.foody.models.Result

/**
 * Recipes type converter
 *
 * We can not insert complex objects in our database directly
 * instead we need to convert them to acceptable types
 */
class RecipesTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe {
        // https://helw.net/2017/11/09/runtime-generics-in-an-erasure-world/
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String): Result {
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, listType)
    }

}