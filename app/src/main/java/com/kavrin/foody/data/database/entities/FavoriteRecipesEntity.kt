package com.kavrin.foody.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kavrin.foody.models.Result
import com.kavrin.foody.util.Constants.FAVORITE_RECIPES_TABLE
                                            // Prevent same Result to be added multiple times
@Entity(tableName = FAVORITE_RECIPES_TABLE, indices = [Index(value = ["result"], unique = true)])
data class FavoriteRecipesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "result")
    val result: Result
)
