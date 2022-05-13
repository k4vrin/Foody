package com.kavrin.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kavrin.foody.models.Result
import com.kavrin.foody.util.Constants.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
data class FavoriteRecipesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val result: Result
)
