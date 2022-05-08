package com.kavrin.foody.di

import com.kavrin.foody.data.network.FoodRecipesApi
import com.kavrin.foody.util.PrConstants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Network module
 *
 * Tell hilt how to provide retrofit for our remote data source
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
@Module // Telling hilt that this object is a module. Class in which you can add bindings for types that cannot be constructor injected.
@InstallIn(SingletonComponent::class) // All bindings inside NetworkModule will be available in SingletonComponent
object NetworkModule {

    /**
     * Provide gson converter factory
     *
     * @return GsonConverterFactory
     *
     * Provide GsonConverterFactory dependency for [provideRetrofitInstance]
     */
    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    /**
     * Provide ok http client
     *
     * @return OkHttpClient
     *
     * Provide OkHttpClient dependency for [provideRetrofitInstance]
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Provide retrofit instance
     *
     * @param okHttpClient
     * @param gsonConverterFactory
     * @return Retrofit
     *
     * Provide Retrofit dependency for [provideApiService]
     */
    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient, // Hilt will find these dependencies from return type of functions that we provide above
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton // We are using application scope for [FoodRecipesApi] that means it will be same instance inside the whole app
    /**
     * Adds a binding for a type that cannot be
     * constructor injected:
     * - Return type is the binding type.
     * - Parameters are dependencies.
     * - Every time an instance is needed, the
     *      function body is executed if the type is not scoped. in this case it is scoped with ApplicationComponent(SingletonComponent)
     *
     * for binding an interface we can use @Binds
     */
    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ): FoodRecipesApi { // By specifying this class as return type we are telling hilt which class we want to inject later
        return retrofit.create(FoodRecipesApi::class.java) // Create an implementation (by retrofit library) of the API endpoints defined by the service interface
    }

}

/**
 * In software engineering, dependency injection is a design pattern in which an object receives
 * other objects that it depends on. A form of inversion of control, dependency injection aims to separate
 * the concerns of constructing objects and using them, leading to loosely coupled programs.
 * The pattern ensures that an object which wants to use a given service should not have to know how to
 * construct those services. Instead, the receiving object (or 'client') is provided with
 * its dependencies by external code (an 'injector'), which it is not aware of
 */

/**
 * https://developer.android.com/training/dependency-injection
 * https://developer.android.com/training/dependency-injection/manual
 */

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