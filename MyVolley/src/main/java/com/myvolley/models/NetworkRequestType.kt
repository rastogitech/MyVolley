package com.myvolley.models

import com.android.volley.Request

/**
 * Holds constants for all supported network request types.
 */
interface NetworkRequestType {

    val GET
        get() = Request.Method.GET
    val POST
        get() = Request.Method.POST
    val PUT
        get() = Request.Method.PUT
    val PATCH
        get() = Request.Method.PATCH
    val DELETE
        get() = Request.Method.DELETE
    val HEAD
        get() = Request.Method.HEAD
    val OPTIONS
        get() = Request.Method.OPTIONS
    val TRACE
        get() = Request.Method.TRACE
}
