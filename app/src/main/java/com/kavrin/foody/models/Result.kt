package com.kavrin.foody.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Result
 * Create data model to parse our sample JSON data with following structure.
 */
@Parcelize // To pass Result to Details Activity by safeArgs. Also id 'kotlin-parcelize' should be added
data class Result(
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int,
    @SerializedName("cheap")
    val cheap: Boolean,
    @SerializedName("dairyFree")
    val dairyFree: Boolean,
    @SerializedName("extendedIngredients")
    val extendedIngredients: @RawValue List<ExtendedIngredient>,
    @SerializedName("glutenFree")
    val glutenFree: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    @SerializedName("sourceName")
    val sourceName: String?, // Can be null
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
) : Parcelable { // To pass Result to Details Activity by safeArgs. Also id 'kotlin-parcelize' should be added

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

    override fun hashCode(): Int {
        var result = aggregateLikes
        result = 31 * result + cheap.hashCode()
        result = 31 * result + dairyFree.hashCode()
        result = 31 * result + extendedIngredients.hashCode()
        result = 31 * result + glutenFree.hashCode()
        result = 31 * result + id
        result = 31 * result + image.hashCode()
        result = 31 * result + readyInMinutes
        result = 31 * result + sourceName.hashCode()
        result = 31 * result + sourceUrl.hashCode()
        result = 31 * result + summary.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + vegan.hashCode()
        result = 31 * result + vegetarian.hashCode()
        result = 31 * result + veryHealthy.hashCode()
        return result
    }
}