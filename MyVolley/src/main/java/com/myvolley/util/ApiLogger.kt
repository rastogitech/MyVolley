package com.myvolley.util

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.myvolley.models.ApiError
import com.myvolley.requests.ApiRequest
import java.lang.reflect.Type

/**
 * Copyright 2018 Rahul Rastogi. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Performs some API logging operations.
 */
object ApiLogger {

    private const val TAG = "MyVolley"
    var isLogEnabled: Boolean = false

    fun logRequest(request: Request<*>) {
        if (!isLogEnabled) {
            return
        }

        var body: String? = null

        if (request is ApiRequest<*>) {
            if (null != request.body) {
                body = String(request.body!!)
            }
        } else {
            try {
                if (null != request.body) {
                    body = String(request.body)
                }
            } catch (authFailureError: AuthFailureError) {
                authFailureError.printStackTrace()
            }
        }

        Log.d(TAG, "Request- Method:" + getMethodTitle(request.method) + " Url:" + request.url
                + " ContentType: " + request.bodyContentType + " requestBody:" + body + " Headers:" + request.headers)
    }


    private fun getMethodTitle(method: Int): String {
        return when (method) {
            Request.Method.DEPRECATED_GET_OR_POST -> "DEPRECATED_GET_OR_POST"
            Request.Method.GET -> "GET"
            Request.Method.POST -> "POST"
            Request.Method.PUT -> "PUT"
            Request.Method.DELETE -> "DELETE"
            Request.Method.HEAD -> "HEAD"
            Request.Method.OPTIONS -> "OPTIONS"
            Request.Method.TRACE -> "TRACE"
            Request.Method.PATCH -> "PATCH"
            else -> "UNKNOWN_METHOD"
        }

    }

    fun logError(error: ApiError?) {
        if (!isLogEnabled) {
            return
        }

        if (null == error) {
            Log.e(TAG, "Could not print error: Null VolleyError object")
            return
        }

        if (null == error.networkResult) {
            Log.e(TAG, "Error- " + error.toString() + " NetworkResponse: null")
        } else {
            Log.e(TAG, "Error- " + error.toString() + " StatusCode:" + error.networkResult.statusCode
                    + " NetworkMS:" + error.networkResult.networkTimeMs)
        }
    }


    fun <R> logApiResponse(url: String, jsonResponse: String, response: NetworkResponse,
                           responseType: Type) {
        if (!isLogEnabled) {
            return
        }

        Log.d(TAG, "Success: StatusCode: " + response.statusCode + " Url:" + url
                + " NetworkMS:" + response.networkTimeMs + " result:" + jsonResponse
                + " Response NetworkRequestType:" + responseType + "\nHeaders:" + response.headers)
    }

    fun d(tag: String, message: String) {
        if (isLogEnabled) {
            Log.d(tag, message)
        }
    }

}
