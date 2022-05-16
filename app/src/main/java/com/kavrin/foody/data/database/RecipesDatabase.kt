package com.kavrin.foody.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kavrin.foody.data.database.entities.FavoriteRecipesEntity
import com.kavrin.foody.data.database.entities.FoodJokeEntity
import com.kavrin.foody.data.database.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class, FavoriteRecipesEntity::class, FoodJokeEntity::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(from = 2, to = 3)], // https://developer.android.com/training/data-storage/room/migrating-db-versions
    exportSchema = true
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

    // Database builder will be inside the database module to tell how to provide it for consumers
}