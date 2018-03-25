package com.myvolleysample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.app.myvolley.listeners.ApiCallback;
import com.app.myvolley.requests.ApiRequest;

import java.util.ArrayList;
import java.util.List;

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
public class MainActivity extends AppCompatActivity {

    private TextView mResponseTV;
    private ProgressDialog progressDialog;
    /**
     * A callback class must be implemented in order to get response of API call. onResponse() and onError()
     * methods will be called according to success/failure of request.
     */
    private ApiCallback<PressureListResponse> mGetPressureListCallback
            = new ApiCallback<PressureListResponse>() {

        @Override
        public void onResponse(PressureListResponse response, NetworkResponse networkResponse) {
            super.onResponse(response, networkResponse);
            progressDialog.dismiss();

            if (null != response.getPressureList() && !response.getPressureList().isEmpty()) {
                mResponseTV.setText("Data count:" + response.getPressureList().size());
            } else {
                Toast.makeText(MainActivity.this, R.string.no_pressure_data, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            super.onErrorResponse(error);
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, R.string.please_try_again, Toast.LENGTH_SHORT).show();
        }
    };
    /**
     * A callback class must be implemented in order to get response of API call. onResponse() and onError()
     * methods will be called according to success/failure of request.
     */
    private ApiCallback<BaseResponse> mPostPressureCallback = new ApiCallback<BaseResponse>() {
        @Override
        public void onResponse(BaseResponse response, NetworkResponse networkResponse) {
            super.onResponse(response, networkResponse);
            progressDialog.dismiss();

            Toast.makeText(MainActivity.this, R.string.msg_pressure_saved, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            super.onErrorResponse(error);
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, R.string.please_try_again, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResponseTV = (TextView) findViewById(R.id.tv_response);
        progressDialog = new ProgressDialog(this);

        //sendGetRequest();

        AlertDialog warningDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.msg_request_warning)
                .setPositiveButton(R.string.ok, null)
                .setCancelable(false).create();
        warningDialog.setCanceledOnTouchOutside(false);
        warningDialog.show();
    }

    /**
     * Sending GET request
     */
    private void sendGetRequest() {
        //TODO: add your url here.
        String getUrl = null;
        progressDialog.show();

        ApiRequest apiRequest = new ApiRequest(Request.Method.GET, getUrl, null,
                PressureListResponse.class, mGetPressureListCallback);
        apiRequest.execute();
    }

    /**
     * Sending POST request
     */
    private void sendPostRequest() {
        progressDialog.show();

        Pressure pressure = new Pressure();
        pressure.setId(1);
        pressure.setDate("2017-07-03T09:56:11.7640232+05:30");
        pressure.setPressures(new double[]{1, 2, 3, 4, 5});

        List<Pressure> pressureList = new ArrayList<>();
        pressureList.add(pressure);

        //TODO: add your URL here.
        String postUrl = null;
        progressDialog.show();

        ApiRequest apiRequest = new ApiRequest(Request.Method.POST, postUrl, pressureList,
                BaseResponse.class, mPostPressureCallback);
        apiRequest.execute();
    }


}
