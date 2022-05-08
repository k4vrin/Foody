package com.kavrin.foody.models


import com.google.gson.annotations.SerializedName

/**
 * Food recipe
 *
 * a list of results from api
 * Create data model to parse our sample JSON data with following structure.
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

    override fun hashCode(): Int {
        return results.hashCode()
    }
}