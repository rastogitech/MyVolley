package com.myvolleylib.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.myvolleylib.models.AuthToken;

/**
 * Copyright 2017 Rahul Rastogi
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
public class ApiSessionManager {

    private static final String SESSION_PREFERENCE = "pref_session";
    private static final String KEY_AUTH_TOKEN = "key_auth_token";
    private static final String VALUE_AUTH_TOKEN = "value_auth_token";

    private static AuthToken sAuthToken;

    private ApiSessionManager() {
    }

    public static void init(Context context) {
        //initializing auth token in class variable from shared preferences.
        SharedPreferences preferences = context.getSharedPreferences(SESSION_PREFERENCE,
                Context.MODE_PRIVATE);
        String tokenKey = preferences.getString(KEY_AUTH_TOKEN, null);
        String token = preferences.getString(VALUE_AUTH_TOKEN, null);

        //if auth token is available.
        if (!TextUtils.isEmpty(token)) {
            sAuthToken = new AuthToken(tokenKey, token);
        }
    }

    /**
     * Saves the session details in shared preferences.
     */
    public static void saveAuthToken(Context context, String key, String token) {
        SharedPreferences preferences = context.getSharedPreferences(SESSION_PREFERENCE,
                Context.MODE_PRIVATE);
        preferences.edit().putString(KEY_AUTH_TOKEN, key).apply();
        preferences.edit().putString(VALUE_AUTH_TOKEN, token).apply();

        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(token)) {
            throw new IllegalArgumentException("Key and token both must have a value.");
        }

        sAuthToken = new AuthToken(key, token);
    }


    public static AuthToken getAuthToken() {
        return sAuthToken;
    }


    public static void removeAuthToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        sAuthToken = null;
    }


}
