package com.kavrin.foody.models


import com.google.gson.annotations.SerializedName

/**
 * Extended ingredient
 *
 * Create data model to parse our sample JSON data with following structure.
 */
data class ExtendedIngredient(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("consistency")
    val consistency: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("unit")
    val unit: String
)