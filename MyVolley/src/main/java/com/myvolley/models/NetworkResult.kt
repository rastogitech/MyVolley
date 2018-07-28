package com.myvolley.models

import com.android.volley.Header
import com.android.volley.NetworkResponse

/**
 * Holds network connection result data.
 */
class NetworkResult constructor(networkResponse: NetworkResponse) {

    /** The HTTP status code.  */
    val statusCode: Int = networkResponse.statusCode

    /** Raw data from this response.  */
    val data: ByteArray = networkResponse.data

    /**
     * Response headers.
     *
     * This map is case-insensitive. It should not be mutated directly.
     *
     * Note that if the server returns two headers with the same (case-insensitive) name, this
     * map will only contain the last one. Use [.allHeaders] to inspect all headers returned
     * by the server.
     */
    val headers: Map<String, String>? = networkResponse.headers

    /** All response headers. Must not be mutated directly.  */
    val allHeaders: List<Header>? = networkResponse.allHeaders

    /** True if the server returned a 304 (Not Modified).  */
    val notModified: Boolean = networkResponse.notModified

    /** Network round trip time in milliseconds.  */
    val networkTimeMs: Long = networkResponse.networkTimeMs
}