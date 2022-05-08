package com.kavrin.foody.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kavrin.foody.data.DataStoreRepository
import com.kavrin.foody.util.Constants.DEFAULT_DIET_TYPE
import com.kavrin.foody.util.Constants.DEFAULT_MEAL_TYPE
import com.kavrin.foody.util.Constants.DEFAULT_RECIPES_NUMBER
import com.kavrin.foody.util.Constants.QUERY_API_KEY
import com.kavrin.foody.util.Constants.QUERY_DIET
import com.kavrin.foody.util.Constants.QUERY_INGREDIENTS
import com.kavrin.foody.util.Constants.QUERY_NUMBER
import com.kavrin.foody.util.Constants.QUERY_RECIPE
import com.kavrin.foody.util.Constants.QUERY_TYPE
import com.kavrin.foody.util.PrConstants.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType


    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        // Collect the values from DataStore and pass it to the query
        viewModelScope.launch {
            readMealAndDietType.collect {
                mealType = it.selectedMealType // If there is no value we pass our Default values
                dietType = it.selectedDietType // that we specified in DataStoreRepository
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_RECIPE] = "true"
        queries[QUERY_INGREDIENTS] = "true"
        Log.d("RecipesBottomSheet", "queries: $queries")
        return queries
    }

    /**
     * Save meal and diet type
     *
     * @param mealType
     * @param mealTypeId
     * @param dietType
     * @param dietTypeId
     *
     * Insert values to the DataStore
     */
    fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
    }
}