package com.kavrin.foody

import com.kavrin.foody.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    /**
     * Get recipes
     *
     * @param queries
     * @return Response of type FoodRecipe
     */
    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        // We need to specifies all queries that we are gonna use with this endpoint
        // QueryMap Will let us create something like hashmap to add all queries at once
        @QueryMap queries: Map<String, String>
    // Wrap FoodRecipe model class with HTTP Response
    ): Response<FoodRecipe>

}