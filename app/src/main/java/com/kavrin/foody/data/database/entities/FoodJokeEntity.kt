package com.kavrin.foody.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kavrin.foody.models.FoodJoke
import com.kavrin.foody.util.Constants.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
data class FoodJokeEntity(
    @PrimaryKey(autoGenerate = false)
    var jokeId: Int = 0,
    // allow nested fields (i.e. fields of the annotated field's class) to be referenced directly in the SQL queries.
    // If the container is an Entity, these sub fields will be columns in the Entity's database table.
    @Embedded
    val joke: FoodJoke
)
