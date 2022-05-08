package com.kavrin.foody.data

import com.kavrin.foody.data.network.FoodRecipesApi
import com.kavrin.foody.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

/**
 * Remote data source
 *
 * Tell hilt how to provide RemoteDataSource for our [Repository]
 *
 * To perform field injection, Hilt needs to know how to provide instances of the necessary dependencies
 * from the corresponding component.
 * A binding contains the information necessary to provide instances of a type as a dependency.
 *
 * One way to provide binding information to Hilt is constructor injection.
 * Use the @Inject annotation on the constructor of a class to tell Hilt how to provide instances of that class
 *
 * The parameters of an annotated constructor of a class are the dependencies of that class
 * Therefore, Hilt must also know how to provide instances of the parameter
 */
class RemoteDataSource @Inject constructor(
    // Hilt will search for FoodRecipesApi provider and since retrofit is an external lib, the dependency is in a module (NetworkModule)
    private val foodRecipesApi: FoodRecipesApi
) {

    /**
     * Get recipes
     *
     * this function will be provided for Repository class
     * it consumes getRecipes that is provided from foodRecipesApi by Hilt
     *
     * The queries will be provided by user(BottomSheet in case of Recipes Fragment) or by default(Constants)
     *
     * @param queries
     * @return
     */
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries = queries)
    }

}