package com.kavrin.foody.di

import com.kavrin.foody.FoodRecipesApi
import com.kavrin.foody.util.Constans.BASE_URL
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
 * Tell hilt how to provide retrofit builder to our remote data source
 */
@Module
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

    @Singleton // We are using application scope for [FoodRecipesApi]
    @Provides // Because e are using retrofit library that is not created by us
    fun provideApiService(retrofit: Retrofit): FoodRecipesApi { // By specifying this class as return type e are telling hilt which class we want to inject later
        return retrofit.create(FoodRecipesApi::class.java)
    }

}