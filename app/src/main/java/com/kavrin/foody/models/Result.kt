package com.kavrin.foody.models


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int,
    @SerializedName("cheap")
    val cheap: Boolean,
    @SerializedName("dairyFree")
    val dairyFree: Boolean,
    @SerializedName("extendedIngredients")
    val extendedIngredients: List<ExtendedIngredient>,
    @SerializedName("glutenFree")
    val glutenFree: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    @SerializedName("sourceName")
    val sourceName: String,
    @SerializedName("sourceUrl")
    val sourceUrl: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("vegan")
    val vegan: Boolean,
    @SerializedName("vegetarian")
    val vegetarian: Boolean,
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean
) {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Result

        if (id != other.id) return false
        if (title != other.title) return false
        if (sourceUrl != other.sourceUrl) return false
        if (sourceName != other.sourceName) return false
        if (image != other.image) return false
        if (vegan != other.vegan) return false
        if (cheap != other.cheap) return false
        if (extendedIngredients != other.extendedIngredients) return false
        if (vegetarian != other.vegetarian) return false
        if (veryHealthy != other.veryHealthy) return false
        if (summary != other.summary) return false
        if (dairyFree != other.dairyFree) return false
        if (readyInMinutes != other.readyInMinutes) return false
        if (aggregateLikes != other.aggregateLikes) return false
        if (glutenFree != other.glutenFree) return false

        return true
    }
}