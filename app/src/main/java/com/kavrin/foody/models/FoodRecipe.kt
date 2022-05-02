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
) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as FoodRecipe

        if (results != other.results) return false
        return true
    }
}