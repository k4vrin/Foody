package com.kavrin.foody.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kavrin.foody.util.Constants.QUERY_API_KEY
import com.kavrin.foody.util.Constants.QUERY_DIET
import com.kavrin.foody.util.Constants.QUERY_INGREDIENTS
import com.kavrin.foody.util.Constants.QUERY_NUMBER
import com.kavrin.foody.util.Constants.QUERY_RECIPE
import com.kavrin.foody.util.Constants.QUERY_TYPE
import com.kavrin.foody.util.PrConstants.API_KEY

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = "main course"
        queries[QUERY_DIET] = "gluten free"
        queries[QUERY_RECIPE] = "true"
        queries[QUERY_INGREDIENTS] = "true"

        return queries
    }


}