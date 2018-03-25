package com.myvolley.managers;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.myvolley.listeners.GlobalApiListener;
import com.myvolley.models.AuthToken;
import com.myvolley.requests.ApiRequest;
import com.myvolley.util.ApiLogger;

/**
 * Copyright 2017 Rahul Rastogi. All Rights Reserved.
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
 */
public class ApiManager {

    private static final int INITIAL_TIMEOUT_MS = 50000;//50 seconds

    private static volatile ApiManager sInstance;
    private RequestQueue mDefaultRequestQueue;
    private GlobalApiListener mGlobalApiListener;
    private RetryPolicy mDefaultRetryPolicy;

    private ApiManager() {
        mDefaultRetryPolicy = new DefaultRetryPolicy(INITIAL_TIMEOUT_MS, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }


    public static ApiManager getInstance() {
        if (null == sInstance) {
            synchronized (ApiManager.class) {
                if (null == sInstance) {
                    sInstance = new ApiManager();
                }
            }
        }
        return sInstance;
    }


    /**
     * This initializes the ApiManager with some basic details. Initialization should be done only once.
     * Most of the times from sub class of Application class.
     *
     * @param context context of app
     */
    public void init(Context context) {
        init(context, Volley.newRequestQueue(context), null);
    }


    /**
     * This initializes the ApiManager with some basic details. Initialization should be done only once. Most of the
     * times from sub class of Application class.
     *
     * @param defaultRequestQueue request queue to handle the cache and network requests
     * @param globalApiListener   listens all the response and error of all api calls before it is actually used
     */
    public void init(Context context, RequestQueue defaultRequestQueue, GlobalApiListener globalApiListener) {
        ApiSessionManager.init(context);

        mDefaultRequestQueue = defaultRequestQueue;
        mGlobalApiListener = globalApiListener;

        mDefaultRequestQueue.start();
    }


    public void setDefaultRetryPolicy(RetryPolicy retryPolicy) {
        mDefaultRetryPolicy = retryPolicy;
    }

    /**
     * Executes a apiRequest
     *
     * @param apiRequest apiRequest to be executed
     */
    public void execute(Request apiRequest) {
        execute(mDefaultRequestQueue, apiRequest, true);
    }

    /**
     * This method will send session details in request header only if @param withSession is true.
     */
    public void executeWithoutSession(Request request) {
        execute(mDefaultRequestQueue, request, false);
    }


    /**
     * Executes a request
     *
     * @param queue       request queue
     * @param request     request to be executed
     * @param withSession whether session details are to be sent in request header or not.
     */
    public void execute(RequestQueue queue, Request request, boolean withSession) {
        if (null == queue){
            throw new IllegalStateException("Either you have not initialized ApiManager or your request queue is null");
        }

        request.setRetryPolicy(mDefaultRetryPolicy);

        //Adding session details.
        AuthToken authToken = ApiSessionManager.getAuthToken();

        if (withSession && null != authToken && request instanceof ApiRequest) {

            ((ApiRequest) request).addHeader(authToken.getKey(), authToken.getToken());
        }

        ApiLogger.logRequest(request);
        queue.add(request);
    }

    public GlobalApiListener getGlobalApiListener() {
        return mGlobalApiListener;
    }

    /**
     * GlobalApiListener listens all the response and errors at first before the response or error is actually used
     *
     * @param globalApiListener listens all the response and errors at first
     */
    public void setGlobalApiListener(GlobalApiListener globalApiListener) {
        mGlobalApiListener = globalApiListener;
    }
}
