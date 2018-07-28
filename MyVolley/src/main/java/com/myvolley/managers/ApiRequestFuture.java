package com.myvolley.managers;

import com.android.volley.Request;
import com.myvolley.listeners.ApiCallback;
import com.myvolley.models.ApiError;
import com.myvolley.models.NetworkResult;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
 * <p>
 * <p>
 *
 * @param <T> The type of parsed response this future expects.
 */
public class ApiRequestFuture<T> extends ApiCallback<T> implements Future<T> {
    private Request<?> mRequest;
    private boolean mResultReceived = false;
    private T mResult;
    private ApiError mApiError;

    public void setRequest(Request<?> request) {
        mRequest = request;
    }

    @Override
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        if (mRequest == null) {
            return false;
        }

        if (!isDone()) {
            mRequest.cancel();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return doGet(null);
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return doGet(TimeUnit.MILLISECONDS.convert(timeout, unit));
    }

    private synchronized T doGet(Long timeoutMs)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (mApiError != null) {
            throw new ExecutionException(mApiError);
        }

        if (mResultReceived) {
            return mResult;
        }

        if (timeoutMs == null) {
            wait(0);
        } else if (timeoutMs > 0) {
            wait(timeoutMs);
        }

        if (mApiError != null) {
            throw new ExecutionException(mApiError);
        }

        if (!mResultReceived) {
            throw new TimeoutException();
        }

        return mResult;
    }

    @Override
    public boolean isCancelled() {
        return mRequest != null && mRequest.isCanceled();
    }

    @Override
    public synchronized boolean isDone() {
        return mResultReceived || mApiError != null || isCancelled();
    }


    @Override
    public void onResponse(T response, NetworkResult networkResult) {
        super.onResponse(response, networkResult);
        mResultReceived = true;
        mResult = response;

        synchronized (this) {
            notifyAll();
        }
    }


    @Override
    public void onErrorResponse(@NotNull ApiError error) {
        super.onErrorResponse(error);
        mApiError = error;
        synchronized (this) {
            notifyAll();
        }
    }
}

