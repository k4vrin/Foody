package com.kavrin.foody.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.kavrin.foody.data.Repository
import com.kavrin.foody.data.database.RecipesEntity
import com.kavrin.foody.models.FoodRecipe
import com.kavrin.foody.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /********************************  ROOM DATABASE  *********************************************/

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipes(recipesEntity)
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipeEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipeEntity)
    }


    /**********************************  RETROFIT  ************************************************/

    private val _recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val recipeResponse: LiveData<NetworkResult<FoodRecipe>> get() = _recipesResponse


    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        _recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries = queries)
                _recipesResponse.value = handleFoodRecipesResponse(response)
                /** Cache Data */
                val foodRecipe = _recipesResponse.value?.data
                if (foodRecipe != null) offlineCacheRecipes(foodRecipe)
                /** Cache Data */
            } catch (e: Exception) {
                _recipesResponse.value = NetworkResult.Error(message = "Recipes not found.")
            }
        } else {
            _recipesResponse.value = NetworkResult.Error(message = "No Internet Connection.")
        }
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        return when {
            // Take long to respond
            response.message().toString()
                .contains("timeout") -> NetworkResult.Error(message = "Timeout")
            // API key got limited
            response.code() == 402 -> NetworkResult.Error(message = "API key limited.")
            // Response is successful but results is empty
            response.body()!!.results.isNullOrEmpty() -> NetworkResult.Error(message = "Recipes not found.")
            // Successful!
            response.isSuccessful -> {
                val foodRecipes = response.body()
                NetworkResult.Success(foodRecipes!!)
            }
            else -> NetworkResult.Error(message = response.message())
        }
    }

    /**
     * Has internet connection
     *
     * Check the internet connection
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>()
            .getSystemService(
                Context.CONNECTIVITY_SERVICE // Use with getSystemService() to retrieve a ConnectivityManager for handling management of network connections.
            ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}