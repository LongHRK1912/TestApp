package com.hrk.test_app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.ui.util.trace
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}

class ConnectivityManagerNetworkMonitor(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : NetworkMonitor {
    override val isOnline: Flow<Boolean> = callbackFlow {
        trace("NetworkMonitor.callbackFlow") {
            val connectivityManager = context.getSystemService<ConnectivityManager>()
            if (connectivityManager == null) {
                trySend(false)
                close()
                return@trace
            }

            val callback = object : NetworkCallback() {
                private val networks = mutableSetOf<Network>()
                override fun onAvailable(network: Network) {
                    networks += network
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    networks -= network
                    trySend(networks.isNotEmpty())
                }
            }

            trace("NetworkMonitor.registerNetworkCallback") {
                val request = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()
                connectivityManager.registerNetworkCallback(request, callback)
            }

            trySend(connectivityManager.isCurrentlyConnected())

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
    }
        .flowOn(ioDispatcher)
        .conflate()

    private fun ConnectivityManager.isCurrentlyConnected() = activeNetwork
        ?.let(::getNetworkCapabilities)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}

