package com.myvolley.managers

import android.content.Context
import android.text.TextUtils
import com.myvolley.models.AuthToken

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
 * This class is used to store auth tokenValue in Shared preferences.
 */
object ApiSessionManager {

    private const val SESSION_PREFERENCE = "pref_session"
    private const val KEY_AUTH_TOKEN = "key_auth_token"
    private const val VALUE_AUTH_TOKEN = "value_auth_token"
    private const val VALUE_REFRESH_TOKEN = "value_refresh_token"

    var authToken: AuthToken? = null
        private set

    fun init(context: Context) {
        //initializing auth tokenValue in class variable from shared preferences.
        val preferences = context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        val tokenKey = preferences.getString(KEY_AUTH_TOKEN, null)
        val token = preferences.getString(VALUE_AUTH_TOKEN, null)

        //if auth tokenValue is available.
        if (!TextUtils.isEmpty(token)) {
            authToken = AuthToken(tokenKey!!, token!!)
        }
    }

    /**
     * Saves the session details in shared preferences.
     */
    fun saveAuthToken(context: Context, key: String, token: String) {
        val preferences = context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        preferences.edit().putString(KEY_AUTH_TOKEN, key).apply()
        preferences.edit().putString(VALUE_AUTH_TOKEN, token).apply()

        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(token)) {
            throw IllegalArgumentException("Token Key and value both must be non-empty.")
        }

        authToken = AuthToken(key, token)
    }

    /**
     * Saves the session details in shared preferences.
     */
    fun saveRefreshToken(context: Context, refreshToken: String) {
        val preferences = context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        preferences.edit().putString(VALUE_REFRESH_TOKEN, refreshToken).apply()
    }

    fun getRefreshToken(context: Context): String? {
        val preferences = context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        return preferences.getString(VALUE_REFRESH_TOKEN, null)
    }

    fun removeSession(context: Context) {
        val preferences = context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        preferences.edit().clear().apply()
        authToken = null
    }

    fun getAuthToken(context: Context): AuthToken? {
        return if (null != authToken) {
            authToken!!.clone() as AuthToken
        } else {
            null
        }
    }


}
