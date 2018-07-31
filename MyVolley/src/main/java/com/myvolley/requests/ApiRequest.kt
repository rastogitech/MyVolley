package com.myvolley.requests

import android.text.TextUtils
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.myvolley.listeners.ApiCallback
import com.myvolley.managers.ApiManager
import com.myvolley.models.ApiError
import com.myvolley.models.NetworkRequestType
import com.myvolley.models.NetworkResult
import com.myvolley.util.ApiLogger
import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.util.*

/**
 * Copyright 2018 Rahul Rastogi. All Rights Reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * A request for retrieving a ResponseType type response by hitting a given URL with optional
 * RequestType data.
 */
class ApiRequest<ResponseType> : com.android.volley.Request<ResponseType>, NetworkRequestType {

    private val mGson = Gson()
    private val mUrl: String
    private var mHeaders: MutableMap<String, String> = HashMap()
    private var mRequestBody: String? = null
    private var mBodyContentType: String? = null
    private var mNetworkResponse: NetworkResponse? = null
    private var mResponseType: Type? = null
    var mApiCallback: ApiCallback<ResponseType>? = null
    private var mUrlEncodedParams: Map<String, String>? = null

    constructor(method: Int, url: String, requestObject: Any?,
                typeToken: TypeToken<ResponseType>,
                apiCallback: ApiCallback<ResponseType>?) : super(method, url, null) {
        mUrl = url
        mRequestBody = if (null != requestObject) mGson.toJson(requestObject) else null
        mResponseType = typeToken.type
        mApiCallback = apiCallback
    }

    constructor(method: Int, url: String, requestObject: Any?,
                responseClass: Class<ResponseType>,
                apiCallback: ApiCallback<ResponseType>?) : super(method, url, null) {
        mUrl = url
        mRequestBody = if (null != requestObject) mGson.toJson(requestObject) else null
        mResponseType = responseClass
        mApiCallback = apiCallback
    }

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        return mHeaders
    }

    /**
     * Sets headers in API request.
     *
     * @param headers, headers with key/value pair.
     */
    fun setHeaders(headers: MutableMap<String, String>) {
        mHeaders = headers
    }

    /**
     * Adds header in API request.
     *
     * @param name,  name of header
     * @param value, value of header
     */
    fun addHeader(name: String, value: String) {
        mHeaders[name] = value
    }

    override fun parseNetworkResponse(response: NetworkResponse): com.android.volley.Response<ResponseType>? {
        var jsonString: String? = null

        try {
            val charset: String = HttpHeaderParser.parseCharset(response.headers, Charsets.UTF_8.toString())
            jsonString = String(response.data, Charset.forName(charset))

            var successResponse: com.android.volley.Response<ResponseType>? = null

            if (null != mResponseType) {
                successResponse = com.android.volley.Response.success(mGson.fromJson<ResponseType>(jsonString, mResponseType),
                        HttpHeaderParser.parseCacheHeaders(response))
            }

            mNetworkResponse = response

            //Logging response
            ApiLogger.logApiResponse<ResponseType>(mUrl, jsonString, response, mResponseType!!)

            return successResponse

        } catch (e: UnsupportedEncodingException) {
            return com.android.volley.Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            //Logging response
            ApiLogger.logApiResponse<Any>(mUrl, jsonString!!, response, mResponseType!!)
            return com.android.volley.Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: ResponseType) {
        val networkResult = NetworkResult(mNetworkResponse!!)

        if (null != ApiManager.globalApiListener) {
            ApiManager.globalApiListener?.onResponse(this, response, networkResult)

        } else if (null != mApiCallback) {
            mApiCallback!!.onResponse(response, networkResult)
        }
    }

    override fun deliverError(error: VolleyError) {
        val apiError = ApiError(error)

        if (null != ApiManager.globalApiListener) {
            ApiManager.globalApiListener?.onErrorResponse(this, apiError)

        } else if (null != mApiCallback) {
            mApiCallback!!.onErrorResponse(apiError)
        }
    }


    @Deprecated("Use {@link #getBodyContentType()}.")
    override fun getPostBodyContentType(): String {
        return bodyContentType
    }


    @Deprecated("Use {@link #getBody()}.")
    override fun getPostBody(): ByteArray? {
        return body
    }

    override fun getBodyContentType(): String {
        return if (TextUtils.isEmpty(mBodyContentType)) PROTOCOL_CONTENT_TYPE else mBodyContentType!!
    }

    fun setBodyContentType(bodyContentType: String) {
        mBodyContentType = bodyContentType
    }

    override fun getBody(): ByteArray? {
        if (PROTOCOL_CONTENT_TYPE == bodyContentType) {
            return try {
                if (mRequestBody == null) null else mRequestBody!!.toByteArray(charset(PROTOCOL_CHARSET))
            } catch (uee: UnsupportedEncodingException) {
                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody,
                        PROTOCOL_CHARSET)
                null
            }
        } else if (FORM_URLENCODED_TYPE == bodyContentType) {
            if (params != null && !params!!.isEmpty()) {
                return encodeParameters(params!!, paramsEncoding)
            }
        }
        return null
    }

    public override fun getParams(): Map<String, String>? {
        return mUrlEncodedParams
    }

    fun setParams(params: Map<String, String>) {
        mUrlEncodedParams = params
    }

    /**
     * This method hits the given url with provided data.
     */
    fun execute() {
        ApiManager.execute(this)
    }

    fun executeWithoutSession() {
        ApiManager.executeWithoutSession(this)
    }

    override fun compareTo(other: Request<ResponseType>?): Int {
        return super.compareTo(other)
    }


    /**
     * This method was private in the com.Android.Volley.Request class. I had to copy it here so as to encode my parameters.
     *
     * @param params,         url encoded parameters
     * @param paramsEncoding, encoding scheme
     * @return url encoded parameters with encoding scheme as an array of byte
     */
    private fun encodeParameters(params: Map<String, String>, paramsEncoding: String): ByteArray {
        val encodedParams = StringBuilder()
        try {
            for ((key, value) in params) {
                encodedParams.append(key)
                encodedParams.append('=')
                encodedParams.append(value)
                encodedParams.append('&')
            }
            return encodedParams.toString().toByteArray(charset(paramsEncoding))
        } catch (uee: UnsupportedEncodingException) {
            throw RuntimeException("Encoding not supported: $paramsEncoding", uee)
        }

    }

    companion object : NetworkRequestType {

        val FORM_URLENCODED_TYPE = "application/x-www-form-urlencoded"
        /**
         * Default charset for JSON request.
         */
        protected val PROTOCOL_CHARSET = "utf-8"
        /**
         * Content type for request.
         */
        private val PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET)
    }

}