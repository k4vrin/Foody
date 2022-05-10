package com.kavrin.foody.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.kavrin.foody.util.Constants.DEFAULT_DIET_TYPE
import com.kavrin.foody.util.Constants.DEFAULT_MEAL_TYPE
import com.kavrin.foody.util.Constants.PREFERENCES_BACK_ONLINE
import com.kavrin.foody.util.Constants.PREFERENCES_DIET_TYPE
import com.kavrin.foody.util.Constants.PREFERENCES_DIET_TYPE_ID
import com.kavrin.foody.util.Constants.PREFERENCES_MEAL_TYPE
import com.kavrin.foody.util.Constants.PREFERENCES_MEAL_TYPE_ID
import com.kavrin.foody.util.Constants.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
 * Data store
 *
 * Create DataStore
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

/**
 * Data store repository
 *
 *
 * Tell hilt how to provide DataStoreRepository for our [com.kavrin.foody.viewmodels.RecipesViewModel]
 *
 * To perform field injection, Hilt needs to know how to provide instances of the necessary dependencies
 * from the corresponding component.
 * A binding contains the information necessary to provide instances of a type as a dependency.
 *
 * One way to provide binding information to Hilt is constructor injection.
 * Use the @Inject annotation on the constructor of a class to tell Hilt how to provide instances of that class
 *
 * The parameters of an annotated constructor of a class are the dependencies of that class
 * Therefore, Hilt must also know how to provide instances of the parameter
 *
 */
@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Preference keys
     *
     * Defining Keys
     */
    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    /**
     * Save meal and diet type
     *
     * @param mealType
     * @param mealTypeId
     * @param dietType
     * @param dietTypeId
     *
     * Specifying keys to values(from chips)
     */
    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    /**
     * Save back online
     *
     * Save backOnline in Datastore (Implemented in RecipesViewModel)
     */
    suspend fun saveBackOnline(backOnline: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }

    /**
     * Read meal and diet type
     *
     * Using Flow to read [MealAndDietType]
     *
     */
    val readMealAndDietType: Flow<MealAndDietType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        // Returns a flow containing the results of applying the given transform function to each value of the original flow
        .map { preferences ->
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0
            // Return
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    /**
     * Read back online
     *
     * Implemented in RecipesViewModel
     */
    val readBackOnline: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        // Returns a flow containing the results of applying the given transform function to each value of the original flow
        .map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)