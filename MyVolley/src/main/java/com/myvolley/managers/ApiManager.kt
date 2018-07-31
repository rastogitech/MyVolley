package com.myvolley.managers

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.Volley
import com.myvolley.listeners.GlobalApiListener
import com.myvolley.requests.ApiRequest
import com.myvolley.requests.multipart.MultipartRequest
import com.myvolley.util.ApiLogger

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
 * Performs basic operations related to Volley network library.
 */
object ApiManager {
    private const val INITIAL_TIMEOUT_MS = 50000//50 seconds

    private var defaultRequestQueue: RequestQueue? = null
    private var mDefaultRetryPolicy: RetryPolicy = DefaultRetryPolicy(INITIAL_TIMEOUT_MS, 0,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    /**
     * GlobalApiListener listens all the response and errors at first before the response or error is actually used
     */
    var globalApiListener: GlobalApiListener? = null

    /**
     * This initializes the ApiManager with some basic details. Initialization should be done only once. Most of the
     * times from sub class of Application class.
     *
     * @param defaultRequestQueue request queue to handle the cache and network requests
     * @param globalListener   listens all the response and error of all api calls before it is actually used
     */
    @JvmOverloads
    fun init(context: Context, globalListener: GlobalApiListener? = null) {
        ApiSessionManager.init(context)

        this.defaultRequestQueue = Volley.newRequestQueue(context)
        this.globalApiListener = globalListener

        defaultRequestQueue!!.start()
    }


    fun setDefaultRetryPolicy(retryPolicy: RetryPolicy) {
        mDefaultRetryPolicy = retryPolicy
    }

    /**
     * Executes a apiRequest
     *
     * @param apiRequest apiRequest to be executed
     */
    fun execute(apiRequest: Request<*>) {
        execute(defaultRequestQueue, apiRequest, true)
    }

    /**
     * This method will send session details in request header only if @param withSession is true.
     */
    fun executeWithoutSession(request: Request<*>) {
        execute(defaultRequestQueue, request, false)
    }


    /**
     * Executes a request
     *
     * @param queue       request queue
     * @param request     request to be executed
     * @param withSession whether session details are to be sent in request header or not.
     */
    private fun execute(queue: RequestQueue?, request: Request<*>, withSession: Boolean) {
        request.retryPolicy = mDefaultRetryPolicy

        //Adding authentication token
        val authToken = ApiSessionManager.authToken

        if (withSession && null != authToken) {
            when (request) {
                is ApiRequest<*> -> request.addHeader(authToken.tokenKey, authToken.tokenValue)
                is MultipartRequest -> request.addHeader(authToken.tokenKey, authToken.tokenValue)
            }
        }

        ApiLogger.logRequest(request)
        queue!!.add(request)
    }
}