package com.kavrin.foody.data

import com.kavrin.foody.data.database.RecipesDao
import com.kavrin.foody.data.database.entities.FavoriteRecipesEntity
import com.kavrin.foody.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Local data source
 *
 * Tell hilt how to provide LocalDataSource for our [Repository]
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
class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    /********************************** Favorite Recipes ***************************************/

    suspend fun insertFavoriteRecipes(favoriteRecipesEntity: FavoriteRecipesEntity) {
        recipesDao.insertFavoriteRecipes(favoriteRecipesEntity)
    }

    fun readFavoriteRecipes(): Flow<List<FavoriteRecipesEntity>> {
        return recipesDao.readFavoriteRecipes()
    }

    suspend fun deleteFavoriteRecipe(favoriteRecipesEntity: FavoriteRecipesEntity) {
        recipesDao.deleteFavoriteRecipe(favoriteRecipesEntity)
    }

    suspend fun deleteAllFavoriteRecipes() {
        recipesDao.deleteAllFavoriteRecipes()
    }
}