package com.kavrin.foody.bindingadapters


import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import com.kavrin.foody.data.database.entities.FoodJokeEntity
import com.kavrin.foody.models.FoodJoke
import com.kavrin.foody.util.NetworkResult

class FoodJokeBinding {

    companion object {

        @BindingAdapter("readJokeApiResponse", "readJokeDatabase", requireAll = false)
        @JvmStatic
        fun setCardAndProgressVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            when (apiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.VISIBLE
                        is MaterialCardView -> view.visibility = View.INVISIBLE
                    }
                }
                is NetworkResult.Success -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is MaterialCardView -> view.visibility = View.VISIBLE
                    }
                }
                else -> { // is Error
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is MaterialCardView -> {
                            if (database != null) {
                                if (database.isEmpty()) {
                                    view.visibility = View.INVISIBLE
                                } else {
                                    view.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }
        }

        @BindingAdapter("readJokeApiResponseErrorView", "readJokeDatabaseErrorView", requireAll = true)
        @JvmStatic
        fun setErrorViewVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            if (database != null && database.isEmpty()) {
                view.visibility = View.VISIBLE
                if (view is TextView  && apiResponse != null) view.text = apiResponse.message.toString()
            }
            if (apiResponse is NetworkResult.Loading) view.visibility = View.INVISIBLE
            if (apiResponse is NetworkResult.Success) view.visibility = View.INVISIBLE
        }
    }
}