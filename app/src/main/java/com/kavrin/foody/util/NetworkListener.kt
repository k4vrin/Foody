package com.kavrin.foody.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Network listener
 *
 * Track Internet connectivity
 */
class NetworkListener : ConnectivityManager.NetworkCallback() {

    private val _isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(context: Context): StateFlow<Boolean> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Make this class default network listener
        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false

        connectivityManager.activeNetwork?.let { network ->
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network)
            networkCapabilities?.let { capabilities ->
                isConnected = when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                    else -> false
                }
            }
        }
        _isNetworkAvailable.value = isConnected
        return _isNetworkAvailable.asStateFlow()
    }

    override fun onAvailable(network: Network) {
        _isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        _isNetworkAvailable.value = false
    }
}