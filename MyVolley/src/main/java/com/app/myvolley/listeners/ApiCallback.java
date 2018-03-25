package com.app.myvolley.listeners;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.myvolley.managers.ApiManager;
import com.app.myvolley.util.ApiLogger;

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
public abstract class ApiCallback<R> implements ResponseListener<R>, Response.ErrorListener {

    /**
     * Receives response of all Gson requests
     * <p/>
     * If all its sub-classes call super.onResponse(response) as the first statement while overriding this method, Then
     * its best resort to carry out initial operations before the response is actually used by overridden method.
     *
     * @param response response received in result
     */
    @Override
    public void onResponse(R response, NetworkResponse networkResponse) {
        if (null != ApiManager.getInstance().getGlobalApiListener()) {
            ApiManager.getInstance().getGlobalApiListener().onResponse(response, networkResponse);
        }
    }


    /**
     * Receives error of all Gson requests
     * <p/>
     * If all its sub-classes call super.onErrorResponse(error) as the first statement while overriding this method, Then
     * its best resort to carry out initial operations before the error is actually used by overridden method.
     *
     * @param error shows details of api call failure
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        ApiLogger.logError(error);

        if (null != ApiManager.getInstance().getGlobalApiListener()) {
            ApiManager.getInstance().getGlobalApiListener().onErrorResponse(error);
        }
    }

}
