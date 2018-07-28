package com.myvolley.listeners

import com.myvolley.models.ApiError
import com.myvolley.models.NetworkResult
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
 * This abstract class is used to listen to the response and error events while dealing with api requests.
 *
 * @param R, type of response
 *
 * */
abstract class ApiCallback<R> : ApiResponseListener<R>, ApiErrorListener {

    /**
     * Receives response of all API requests.
     *
     * If all its sub-classes call super.onResponse(response) as the first statement while overriding this method, Then
     * its best resort to carry out initial operations before the response is actually used by overridden method.
     *
     * @param response response received in result
     */
    override fun onResponse(response: R?, networkResult: NetworkResult) {}

    /**
     * Receives error of all API requests.
     *
     * If all its sub-classes call super.onErrorResponse(error) as the first statement while overriding this method, then
     * it's best resort to carry out initial operations before the error is actually used by overridden method.
     *
     * @param error shows details of api call failure
     */
    override fun onErrorResponse(error: ApiError) {
        ApiLogger.logError(error)
    }
}
