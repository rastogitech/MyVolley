package com.myvolley.util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.myvolley.requests.ApiRequest;

import java.lang.reflect.Type;

/**
 * Copyright 2017 Rahul Rastogi. All Rights Reserved.
 * <p/>
 * Performs some logging operations.
 */
public class ApiLogger {

    private static final String TAG = "Volley";
    private static boolean mLogEnabled = true;

    public static boolean isLogEnabled() {
        return mLogEnabled;
    }

    public static void setLogEnabled(boolean enable) {
        mLogEnabled = enable;
    }

    public static void logRequest(Request request) {
        if (!mLogEnabled) {
            return;
        }

        String body = null;

        if (request instanceof ApiRequest) {
            ApiRequest apiRequest = (ApiRequest) request;

            if (null != apiRequest.getBody()) {
                body = new String(apiRequest.getBody());
            }
        } else {
            try {
                if (null != request.getBody()) {
                    body = new String(request.getBody());
                }
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }
        }

        Log.d(TAG, "Request- Method:" + getMethodTitle(request.getMethod()) + " Url:" + request.getUrl()
                + " ContentType: " + request.getBodyContentType() + " requestBody:" + body);
    }


    private static String getMethodTitle(int method) {
        switch (method) {
            case Request.Method.DEPRECATED_GET_OR_POST:
                return "DEPRECATED_GET_OR_POST";
            case Request.Method.GET:
                return "GET";
            case Request.Method.POST:
                return "POST";
            case Request.Method.PUT:
                return "PUT";
            case Request.Method.DELETE:
                return "DELETE";
            case Request.Method.HEAD:
                return "HEAD";
            case Request.Method.OPTIONS:
                return "OPTIONS";
            case Request.Method.TRACE:
                return "TRACE";
            case Request.Method.PATCH:
                return "PATCH";
            default:
                return "UNKNOWN_METHOD";
        }

    }

    public static void logError(VolleyError error) {
        if (!mLogEnabled) {
            return;
        }

        if (null == error) {
            Log.e(TAG, "Could not print error: Null VolleyError object");
            return;
        }

        if (null == error.networkResponse) {
            Log.e(TAG, "Error- " + error.toString() + " NetworkResponse: null");
        } else {
            Log.e(TAG, "Error- " + error.toString() + " StatusCode:" + error.networkResponse.statusCode
                    + " NetworkMS:" + error.networkResponse.networkTimeMs);
        }
    }


    public static <R> void logGsonResponse(String url, String jsonResponse, NetworkResponse response,
                                           Type responseType) {
        if (!mLogEnabled) {
            return;
        }

        Log.d(TAG, "Success: StatusCode: " + response.statusCode + " Url:" + url
                + " NetworkMS:" + response.networkTimeMs + " result:" + jsonResponse
                + " Response Type:" + responseType);
    }

}
