package com.kavrin.foody.util

/**
 * Network result
 *
 * @param T
 * @property data
 * @property message
 *
 * A sealed class that contains Success, Error and Loading classes to handle api response
 * that will be used in MainVieModel
 */
sealed class NetworkResult<T>(
    val data: T? = null, // Actual data from api(Response.body)
    val message: String? = null // Response.message
) {

    class Success<T>(data: T) : NetworkResult<T>(data = data)
    class Error<T>(data: T? = null, message: String?) :
        NetworkResult<T>(data = data, message = message)

    class Loading<T> : NetworkResult<T>()

}
