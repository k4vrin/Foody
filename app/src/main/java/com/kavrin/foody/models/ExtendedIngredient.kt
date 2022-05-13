package com.kavrin.foody.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Extended ingredient
 *
 * Create data model to parse our sample JSON data with following structure.
 */
@Parcelize // To pass Result to Details Activity by safeArgs. Also id 'kotlin-parcelize' should be added
data class ExtendedIngredient(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("consistency")
    val consistency: String?, // Can be null
    @SerializedName("image")
    val image: String?, // Can be null
    @SerializedName("name")
    val name: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("unit")
    val unit: String
) : Parcelable // To pass Result to Details Activity by safeArgs. Also id 'kotlin-parcelize' should be added