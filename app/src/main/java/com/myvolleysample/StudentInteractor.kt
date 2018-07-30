/*
 * Copyright (c) 2018 Rahul Rastogi. All Rights Reserved.
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

package com.myvolleysample

import com.myvolley.listeners.ApiCallback
import com.myvolley.requests.ApiRequest
import com.myvolleysample.models.BaseResponse
import com.myvolleysample.models.Student
import com.myvolleysample.models.StudentListResponse

/**
 * Handles Student related API requests.
 */
class StudentInteractor {

    /**
     * Sends GET type of request to get a list of students.
     */
    fun getStudentList(callback: ApiCallback<StudentListResponse>) {
        val getStudentListUrl = "" //TODO: edit url here
        val apiRequest = ApiRequest(ApiRequest.GET, getStudentListUrl, null,
                StudentListResponse::class.java, callback)
        apiRequest.execute()
    }

    /**
     * Sends POST type of request to save student object.
     */
    fun saveStudent(student: Student, callback: ApiCallback<BaseResponse>) {
        val saveStudentUrl = "" //TODO: edit url here

        val apiRequest = ApiRequest(ApiRequest.POST, saveStudentUrl, student, BaseResponse::class.java, callback)
        apiRequest.execute()
    }

}