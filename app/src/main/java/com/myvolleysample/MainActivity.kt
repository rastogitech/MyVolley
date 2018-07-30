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
import com.myvolleysample.models.BaseResponse
import com.myvolleysample.models.Student
import com.myvolleysample.models.StudentListResponse

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

    private lateinit var mResponseTV: TextView
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mResponseTV = findViewById(R.id.tv_response)
        progressDialog = ProgressDialog(this)

        val warningDialog = AlertDialog.Builder(this)
                .setMessage(R.string.msg_request_warning)
                .setPositiveButton(R.string.ok, null)
                .setCancelable(false).create()
        warningDialog.setCanceledOnTouchOutside(false)
        warningDialog.show()
    }


    private fun getStudentList() {
        progressDialog.show()
        StudentInteractor().getStudentList(GetStudentListCallback())
    }


    /**
     * A callback class must be implemented in order to get response of API call. onResponse() and onError()
     * methods will be called according to success/failure of request.
     */
    inner class GetStudentListCallback : ApiCallback<StudentListResponse>() {

        override fun onResponse(response: StudentListResponse?, networkResult: NetworkResult) {
            super.onResponse(response, networkResult)
            progressDialog.dismiss()

            if (null != response!!.studentList && !response.studentList!!.isEmpty()) {
                mResponseTV.text = "Data count: ${response.studentList!!.size}"
            } else {
                Toast.makeText(this@MainActivity, R.string.no_data_available, Toast.LENGTH_SHORT).show()
            }
        }

        override fun onErrorResponse(error: ApiError) {
            super.onErrorResponse(error)
            progressDialog.dismiss()
            Toast.makeText(this@MainActivity, R.string.please_try_again, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Sending POST request
     */
    private fun saveStudent() {
        val student = Student()
        student.id = 1
        student.name = "Allen Solly"

        progressDialog.show()
        StudentInteractor().saveStudent(student, SaveStudentCallback())
    }


    /**
     * A callback class must be implemented in order to get response of API call. onResponse() and onError()
     * methods will be called according to success/failure of request.
     */
    private inner class SaveStudentCallback : ApiCallback<BaseResponse>() {

        override fun onResponse(response: BaseResponse?, networkResult: NetworkResult) {
            super.onResponse(response, networkResult)
            progressDialog.dismiss()

            if (response!!.status) {
                Toast.makeText(this@MainActivity, R.string.saved_successfully, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, R.string.please_try_again, Toast.LENGTH_SHORT).show()
            }
        }

        override fun onErrorResponse(error: ApiError) {
            super.onErrorResponse(error)
            progressDialog.dismiss()
            Toast.makeText(this@MainActivity, R.string.please_try_again, Toast.LENGTH_SHORT).show()
        }
    }

}
