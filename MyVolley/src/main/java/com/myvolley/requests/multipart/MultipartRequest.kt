/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.myvolley.requests.multipart

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
import org.apache.http.HttpEntity
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntityBuilder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
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
 * This class enables sending multipart/form-data type of requests for uploading files etc.
 */
class MultipartRequest<ResponseType> : Request<ResponseType> {

    var mApiCallback: ApiCallback<ResponseType>? = null
    private val mHeaders: MutableMap<String, String> = HashMap()
    private val mGson = Gson()
    private var mNetworkResponse: NetworkResponse? = null
    private val mUrl: String
    private var mResponseType: Type? = null
    private val entityBuilder = MultipartEntityBuilder.create()
    private lateinit var httpEntity: HttpEntity

    constructor(method: Int, url: String, responseType: TypeToken<ResponseType>?,
                apiCallback: ApiCallback<ResponseType>) : super(method, url, null) {
        mUrl = url
        mApiCallback = apiCallback
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
        entityBuilder.setBoundary(generateBoundary())

        if (responseType != null) {
            mResponseType = responseType.type
        }
    }

    constructor(method: Int, url: String, responseType: Class<ResponseType>,
                apiCallback: ApiCallback<ResponseType>) : super(method, url, null) {
        mUrl = url
        mResponseType = responseType
        mApiCallback = apiCallback
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
        entityBuilder.setBoundary(generateBoundary())
    }

    private fun generateBoundary(): String {
        val buffer = StringBuilder()
        val rand = Random()
        val count = rand.nextInt(11) + 30

        for (i in 0 until count) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.size)])
        }

        return buffer.toString()
    }

    /**
     * Adds text key/value pair to be sent into request.
     */
    fun addTextData(key: String, value: String, contentType: ContentType = ContentType.TEXT_PLAIN) {
        entityBuilder.addTextBody(key, value, contentType)
    }

    /**
     * Adds text key/file pair to be sent into request.
     */
    fun addFileData(key: String, fileName: String, file: File, contentType: ContentType = ContentType.MULTIPART_FORM_DATA) {
        entityBuilder.addBinaryBody(key, file, contentType, fileName)
    }

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        return if (!mHeaders.isEmpty()) mHeaders else super.getHeaders()
    }

    /**
     * Sets header for API request.
     *
     * @param headers, headers with key/value pair.
     */
    fun setHeaders(headers: MutableMap<String, String>) {
        mHeaders.clear()
        mHeaders.putAll(headers)
    }

    fun addHeader(name: String, value: String) {
        mHeaders[name] = value
    }

    override fun getBodyContentType(): String {
        return httpEntity.contentType.value
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray {
        httpEntity = entityBuilder.build()
        val byteArrayOutputStream = ByteArrayOutputStream()

        try {
            httpEntity.writeTo(byteArrayOutputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return byteArrayOutputStream.toByteArray()
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<ResponseType>? {
        mNetworkResponse = response
        var jsonString: String? = null

        try {
            val charset: String = HttpHeaderParser.parseCharset(response.headers,
                    Charsets.UTF_8.toString())

            jsonString = String(response.data, Charset.forName(charset))
            var successResponse: com.android.volley.Response<ResponseType>? = null

            if (null != mResponseType) {
                successResponse = com.android.volley.Response.success(mGson.fromJson<ResponseType>(jsonString, mResponseType),
                        HttpHeaderParser.parseCacheHeaders(response))
            }

            //Logging response
            ApiLogger.logApiResponse<Any>(mUrl, jsonString, response, mResponseType!!)
            return successResponse

        } catch (e: UnsupportedEncodingException) {
            return com.android.volley.Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            //Logging response
            ApiLogger.logApiResponse<ResponseType>(mUrl, jsonString!!, response, mResponseType!!)
            return com.android.volley.Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: ResponseType) {
        if (null != mApiCallback) {
            mApiCallback!!.onResponse(response, NetworkResult(mNetworkResponse!!))
        }
    }

    override fun deliverError(error: VolleyError) {
        if (null != mApiCallback) {
            mApiCallback!!.onErrorResponse(ApiError(error))
        }
    }

    override fun compareTo(other: Request<ResponseType>?): Int {
        return super.compareTo(other)
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

    companion object : NetworkRequestType {
        private val MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    }
}
