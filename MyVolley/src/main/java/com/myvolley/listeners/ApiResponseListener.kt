package com.myvolley.listeners

import com.myvolley.models.NetworkResult

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
 * Interface definition for a callback to be invoked when API response is received.
 * @param R, type of response
 */
interface ApiResponseListener<R> {

    /**
     * @param response, response from API.
     * @param networkResult, contains network related details like: response headers, response code, API execution time (in Milliseconds)
     */
    fun onResponse(response: R?, networkResult: NetworkResult)
}
