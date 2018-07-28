package com.myvolley.models

import com.android.volley.*

/**
 * Holds API connection error.
 */
class ApiError constructor(volleyError: VolleyError) : Exception(volleyError) {

    val networkResult: NetworkResult? = if (volleyError.networkResponse != null) NetworkResult(volleyError.networkResponse) else null
    val errorType: String

    init {
        errorType = when (volleyError) {
            is NoConnectionError -> NO_CONNECTION_ERROR
            is TimeoutError -> TIMEOUT_ERROR
            is NetworkError -> NETWORK_ERROR
            is ClientError -> CLIENT_ERROR
            is ServerError -> SERVER_ERROR
            else -> UNKNOWN_ERROR
        }
    }

    companion object {
        const val NO_CONNECTION_ERROR = "no_connection_error"
        const val TIMEOUT_ERROR = "timeout_error"
        const val NETWORK_ERROR = "network_error"
        const val CLIENT_ERROR = "client_error"
        const val SERVER_ERROR = "server_error"
        const val UNKNOWN_ERROR = "unknown_error"
    }
}