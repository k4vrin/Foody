package com.kavrin.foody.data.database

import androidx.room.*
import com.kavrin.foody.data.database.entities.FavoriteRecipesEntity
import com.kavrin.foody.data.database.entities.FoodJokeEntity
import com.kavrin.foody.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // When we fetch new data we want to replace the old one
    suspend fun insertRecipes(recipesEntity: RecipesEntity) // Will be implemented in MainViewModel

    @Query(value = "SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>> // Will be implemented in MainViewModel
    // Data inside the Flow is circulating whenever we receive some new value

    /********************************** Favorite Recipes ***************************************/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipes(favoriteRecipesEntity: FavoriteRecipesEntity)

    @Query(value = "SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavoriteRecipesEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoriteRecipesEntity: FavoriteRecipesEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()

    /********************************** Food Joke ***************************************/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query(value = "SELECT * FROM food_joke_table ORDER BY RANDOM() LIMIT 1") // We don't need this random. just for reference
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>
}