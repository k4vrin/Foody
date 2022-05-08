package com.kavrin.foody.di

import android.content.Context
import androidx.room.Room
import com.kavrin.foody.data.database.RecipesDatabase
import com.kavrin.foody.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Database module
 *
 * Tell hilt how to provide Dao to our local data source
 *
 * Sometimes a type cannot be constructor-injected. This can happen for multiple reasons.
 * For example, you cannot constructor-inject an interface.
 * You also cannot constructor-inject a type that you do not own, such as a class from an external library.
 * In these cases, you can provide Hilt with binding information by using Hilt modules.
 *
 * A Hilt module is a class that is annotated with @Module. Like a Dagger module, it informs Hilt
 * how to provide instances of certain types. Unlike Dagger modules, you must annotate Hilt modules with
 * @InstallIn to tell Hilt which Android class each module will be used or installed in.
 *
 * Dependencies that you provide in Hilt modules are available in all generated components
 * that are associated with the Android class where you install the Hilt module
 *
 * Note: Because Hilt's code generation needs access to all of the Gradle modules that use Hilt,
 * the Gradle module that compiles your Application class also needs to have all of your Hilt modules
 * and constructor-injected classes in its transitive dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) =
        Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            DATABASE_NAME
        ).build()


    @Singleton
    @Provides
    fun provideDao(database: RecipesDatabase) = database.recipesDao()
}

/**
 * @Provide
 *
 * Interfaces are not the only case where you cannot constructor-inject a type.
 * Constructor injection is also not possible if you don't own the class because
 * it comes from an external library (classes like Retrofit, OkHttpClient, or Room databases),
 * or if instances must be created with the builder pattern.
 *
 * The annotated function supplies the following information to Hilt:
 *
 * The function return type tells Hilt what type the function provides instances of.
 *
 * The function parameters tell Hilt the dependencies of the corresponding type.
 *
 * The function body tells Hilt how to provide an instance of the corresponding type.
 * Hilt executes the function body every time it needs to provide an instance of that type.
 */