package com.kavrin.foody.models


import com.google.gson.annotations.SerializedName

/**
 * Food recipe
 *
 * a list of results from api
 */
data class FoodRecipe(
    @SerializedName("results")
    val results: List<Result>
)