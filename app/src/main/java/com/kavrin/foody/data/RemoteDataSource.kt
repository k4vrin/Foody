package com.kavrin.foody.data

import com.kavrin.foody.data.network.FoodRecipesApi
import com.kavrin.foody.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

/**
 * Remote data source
 *
 * Request data from Api
 */
class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi // Hilt will search for FoodRecipesApi provider
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries = queries)
    }

}


/**
 * https://developer.android.com/training/dependency-injection
 * https://developer.android.com/training/dependency-injection/manual
 *
 *
 */