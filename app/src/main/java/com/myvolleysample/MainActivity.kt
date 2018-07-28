package com.myvolleysample

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.myvolley.listeners.ApiCallback
import com.myvolley.models.ApiError
import com.myvolley.models.NetworkResult
import com.myvolley.requests.ApiRequest
import com.myvolleysample.models.BaseResponse
import com.myvolleysample.models.Pressure
import com.myvolleysample.models.PressureListResponse
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
 */
class MainActivity : AppCompatActivity() {

    private var mResponseTV: TextView? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mResponseTV = findViewById(R.id.tv_response)
        progressDialog = ProgressDialog(this)

        //sendGetRequest();

        val warningDialog = AlertDialog.Builder(this)
                .setMessage(R.string.msg_request_warning)
                .setPositiveButton(R.string.ok, null)
                .setCancelable(false).create()
        warningDialog.setCanceledOnTouchOutside(false)
        warningDialog.show()
    }


    /**
     * Sending GET request
     */
    private fun sendGetRequest() {
        //TODO: add your url here.
        val getUrl: String? = null
        progressDialog!!.show()

        val apiRequest = ApiRequest(ApiRequest.GET, getUrl!!, null,
                PressureListResponse::class.java, GetPressureListCallback())
        apiRequest.execute()
    }

    /**
     * A callback class must be implemented in order to get response of API call. onResponse() and onError()
     * methods will be called according to success/failure of request.
     */
    private inner class GetPressureListCallback : ApiCallback<PressureListResponse>() {

        override fun onResponse(response: PressureListResponse?, networkResult: NetworkResult) {
            super.onResponse(response, networkResult)
            progressDialog!!.dismiss()

            if (null != response!!.pressureList && !response.pressureList!!.isEmpty()) {
                mResponseTV!!.text = "Data count:" + response.pressureList!!.size
            } else {
                Toast.makeText(this@MainActivity, R.string.no_pressure_data, Toast.LENGTH_SHORT).show()
            }
        }

        override fun onErrorResponse(error: ApiError) {
            super.onErrorResponse(error)
            progressDialog!!.dismiss()
            Toast.makeText(this@MainActivity, R.string.please_try_again, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Sending POST request
     */
    private fun sendPostRequest() {
        progressDialog!!.show()

        val pressure = Pressure()
        pressure.id = 1
        pressure.date = "2017-07-03T09:56:11.7640232+05:30"
        pressure.pressures = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0)

        val pressureList = ArrayList<Pressure>()
        pressureList.add(pressure)

        //TODO: add your URL here.
        val postUrl: String? = null
        progressDialog!!.show()

        val apiRequest = ApiRequest(ApiRequest.POST, postUrl!!, pressureList,
                BaseResponse::class.java, mPostPressureCallback)
        apiRequest.execute()
    }


    /**
     * A callback class must be implemented in order to get response of API call. onResponse() and onError()
     * methods will be called according to success/failure of request.
     */
    private val mPostPressureCallback = object : ApiCallback<BaseResponse>() {
        override fun onResponse(response: BaseResponse?, networkResult: NetworkResult) {
            super.onResponse(response, networkResult)
            progressDialog!!.dismiss()

            Toast.makeText(this@MainActivity, R.string.msg_pressure_saved, Toast.LENGTH_SHORT).show()
        }

        override fun onErrorResponse(error: ApiError) {
            super.onErrorResponse(error)
            progressDialog!!.dismiss()
            Toast.makeText(this@MainActivity, R.string.please_try_again, Toast.LENGTH_SHORT).show()
        }
    }

}
