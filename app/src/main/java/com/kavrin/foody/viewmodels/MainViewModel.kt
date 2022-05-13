package com.kavrin.foody.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.kavrin.foody.data.Repository
import com.kavrin.foody.data.database.entities.FavoriteRecipesEntity
import com.kavrin.foody.data.database.entities.RecipesEntity
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
) : AndroidViewModel(application) { // Because we need application context

    /********************************  ROOM DATABASE  *********************************************/

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    val readFavoriteRecipes: LiveData<List<FavoriteRecipesEntity>> = repository.local.readFavoriteRecipes().asLiveData()

    fun insertFavoriteRecipes(favoriteRecipesEntity: FavoriteRecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoriteRecipesEntity)
        }

    fun deleteFavoriteRecipe(favoriteRecipesEntity: FavoriteRecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoriteRecipesEntity)
        }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }

    /**
     * Offline cache recipes
     *
     * @param foodRecipe
     *
     * If FoodRecipes from [getRecipesSafeCall] is not null, it will be inserted to database
     * and will be read by [readRecipes]
     */
    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipeEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipeEntity)
    }


    /**********************************  RETROFIT  ************************************************/

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        return when {
            // Take long to respond
            response.message().toString()
                .contains("timeout") -> NetworkResult.Error(message = "Timeout")
            // API key got limited
            response.code() == 402 -> NetworkResult.Error(message = "API key limited.")
            // Response is successful but results is empty
            response.body()!!.results.isNullOrEmpty() -> NetworkResult.Error(message = "Recipes not found.") // If body is null, it will be handled in getRecipesSafeCall
            // Successful!
            response.isSuccessful -> {
                val foodRecipes = response.body()
                NetworkResult.Success(foodRecipes!!)
            }
            else -> NetworkResult.Error(message = response.message())
        }
    }

    /*************  Recipes Response  ***************/

    private val _recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val recipeResponse: LiveData<NetworkResult<FoodRecipe>> get() = _recipesResponse


    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        _recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) { // Than we want to make GET request to our Api
            try {
                val response = repository.remote.getRecipes(queries = queries) // Actual GET request
                _recipesResponse.value = handleFoodRecipesResponse(response)
                /****** Cache Data ******/
                val foodRecipe = _recipesResponse.value?.data
                if (foodRecipe != null) offlineCacheRecipes(foodRecipe)
                /****** Cache Data ******/
            } catch (e: Exception) {
                _recipesResponse.value = NetworkResult.Error(message = "Recipes not found.")
            }
        } else { // Otherwise Error
            _recipesResponse.value = NetworkResult.Error(message = "No Internet Connection.")
        }
    }

    /*************  Search Response  ***************/

    private val _searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val searchRecipesResponse: LiveData<NetworkResult<FoodRecipe>> get() =  _searchRecipesResponse

    fun searchRecipes(searchQueries: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQueries)
    }

    private suspend fun searchRecipesSafeCall(searchQueries: Map<String, String>) {
        _searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) { // Than we want to make GET request to our Api
            try {
                val response = repository.remote.searchRecipes(searchQueries = searchQueries) // Actual GET request
                _searchRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                _searchRecipesResponse.value = NetworkResult.Error(message = "Recipes not found.")
            }
        } else { // Otherwise Error
            _searchRecipesResponse.value = NetworkResult.Error(message = "No Internet Connection.")
        }
    }


    /**
     * Has internet connection
     *
     * Check the internet connection
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>() // AndroidViewModel
            .getSystemService(
                Context.CONNECTIVITY_SERVICE // Use with getSystemService() to retrieve a ConnectivityManager for handling management of network connections.
            ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
            else -> false
        }
    }
}