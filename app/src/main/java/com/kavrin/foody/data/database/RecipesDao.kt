package com.kavrin.foody.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // When we fetch new data we want to replace the old one
    suspend fun insertRecipes(recipesEntity: RecipesEntity) // Will be implemented in MainViewModel

    @Query(value = "SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>> // Will be implemented in MainViewModel
    // Data inside the Flow is circulating whenever we receive some new value
}