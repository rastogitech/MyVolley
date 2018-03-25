package com.app.myvolley.requests;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.app.myvolley.listeners.ApiCallback;
import com.app.myvolley.managers.ApiManager;
import com.app.myvolley.util.ApiLogger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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
public class ApiRequest extends com.android.volley.Request {

    /**
     * Default charset for JSON request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private Gson mGson = new Gson();
    private String mUrl;
    private Map<String, String> mHeaders = new HashMap<>();
    private String mRequestBody;
    private String mBodyContentType;
    private NetworkResponse mNetworkResponse;
    private Type mResponseType;
    private ApiCallback mApiCallback;

    public <Request, Response> ApiRequest(int method, String url, Request requestObject,
                                          TypeToken<Response> typeToken,
                                          ApiCallback<Response> apiCallback) {
        super(method, url, apiCallback);
        mUrl = url;
        mRequestBody = null != requestObject ? mGson.toJson(requestObject) : null;
        if (null != typeToken) {
            mResponseType = typeToken.getType();
        }
        mApiCallback = apiCallback;
    }

    public <RequestType, ResponseType> ApiRequest(int method, String url, RequestType requestObject,
                                                  Class<ResponseType> responseClass,
                                                  ApiCallback<ResponseType> apiCallback) {
        super(method, url, apiCallback);
        mUrl = url;
        mRequestBody = null != requestObject ? mGson.toJson(requestObject) : null;
        mResponseType = responseClass;
        mApiCallback = apiCallback;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }


    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    public void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }


    @Override
    protected com.android.volley.Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers,
                    PROTOCOL_CHARSET));
            com.android.volley.Response successResponse = null;

            if (null != mResponseType) {
                successResponse = com.android.volley.Response.success(mGson.fromJson(jsonString, mResponseType),
                        HttpHeaderParser.parseCacheHeaders(response));
            }

            mNetworkResponse = response;

            //Logging response
            ApiLogger.logGsonResponse(mUrl, jsonString, response, mResponseType);

            return successResponse;

        } catch (UnsupportedEncodingException e) {
            return com.android.volley.Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return com.android.volley.Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        mApiCallback.onResponse(response, mNetworkResponse);
    }

    /**
     * @deprecated Use {@link #getBodyContentType()}.
     */
    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    /**
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public byte[] getPostBody() {
        return getBody();
    }


    @Override
    public String getBodyContentType() {
        return TextUtils.isEmpty(mBodyContentType) ? PROTOCOL_CONTENT_TYPE : mBodyContentType;
    }


    public void setBodyContentType(String bodyContentType) {
        mBodyContentType = bodyContentType;
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody,
                    PROTOCOL_CHARSET);
            return null;
        }
    }

    /**
     * This method hits the given url with provided data.
     */
    public void execute() {
        ApiManager.getInstance().execute(this);
    }

    public void executeWithoutSession() {
        ApiManager.getInstance().executeWithoutSession(this);
    }

    public void execute(RequestQueue requestQueue, boolean withSession) {
        ApiManager.getInstance().execute(requestQueue, this, withSession);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}