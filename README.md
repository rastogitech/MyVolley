# MyVolley-Kotlin
This library makes easy to send network requests having Json data with java/kotlin objects directly without requiring any manual conversion from Java/Kotlin objects to json. You can send Multipart/form-data request too using this library. This library uses Volley library internally to send network requests.

**1. Add following two dependencies in your module's build.gradle file using:**

`dependencies {`
    `implementation 'com.myvolley:MyVolley:1.0.7'`
    `implementation 'com.google.code.gson:gson:2.8.2'`
`}`

Note that, if you face any error like: **More than one file was found with OS independent path 'META-INF/DEPENDENCIES'** then add following lines in you module's build.gradle file:

`android.packagingOptions {`
    `exclude 'META-INF/DEPENDENCIES'`
    `exclude 'META-INF/LICENSE'`
    `exclude 'META-INF/NOTICE'`
`}`

**For GET request:**
**Prepare your request model:**
Suppose, you want to call an API which returns Json object in response which might look like:

`{`
	`"Students": [{`
		`"Id": 1,`
		`"Name": "Allen solly"`
	`}, {`
		`"Id": 2,`
		`"Name": "Mac"`
	`}, {`
		`"Id": 3,`
		`"Name": "J. Smith"`
	`}],`
	`"Status": true,`
	`"Message": "Success"`
`}`

So, for this this type of response you need to create two model classes. First is for the Json objects which are coming in list i.e.
`class Student {`
    `@SerializedName("Id")`
    `var id: Int = 0`

    `@SerializedName("Name")`
    `var name: String? = null`
`}`

And main class is to wrap up the Json array of type Student, status and message properties i.e.

class StudentListResponse {
    @SerializedName("Result")
    var studentList: List<Student>? = null
    
    @SerializedName("Status")
    var status: Boolean = false

    @SerializedName("Message")
    var message: String? = null
}

Now, prepare a callback to receive response/error of API call:

    `inner class GetStudentListCallback : ApiCallback<StudentListResponse>() {`

        `override fun onResponse(response: StudentListResponse?, networkResult: NetworkResult) {`
            `super.onResponse(response, networkResult)`
            `progressDialog.dismiss()`

            `if (null != response!!.studentList && !response.studentList!!.isEmpty()) {`
                `Toast.makeText(context, "Total students are: ${response.studentList!!.size}", Toast.LENGTH_SHORT).show()`
            `} else {`
                `Toast.makeText(class, R.string.no_data_available, Toast.LENGTH_SHORT).show()`
            `}`
        `}`

        `override fun onErrorResponse(error: ApiError) {`
            `super.onErrorResponse(error)`
            `progressDialog.dismiss()`
            `Toast.makeText(this@MainActivity, R.string.please_try_again, Toast.LENGTH_SHORT).show()`
        `}`
    `}`

**Finally, API request to GET student list for above Json would look like:**

    `val getStudentListUrl = "http://url_to_get_student_list" //This is the url which is expected to give student list`

    `val apiRequest = ApiRequest(ApiRequest.GET, getStudentListUrl, null, StudentListResponse::class.java, callback)`
    `apiRequest.execute()`


**For POST request:**
**Prepare request Json model:**
Let's suppose we want to save a Student object on server. For this, request Json may look like:

`{`
    `"Id": 1,`
    `"Name": "Allen solly"`
`}`

Corresponding class for above Json will be:

`class Student {`
    `@SerializedName("Id")`
    `var id: Int = 0`

    `@SerializedName("Name")`
    `var name: String? = null`
`}`

And if response json is:

`{`
    `"Status": true,`
    `"Message": "Success"`
`}`

Related class for above response may be:

class BaseResponse {
    @SerializedName("Status")
    var status: Boolean = false

    @SerializedName("Message")
    var message: String? = null
}

Now, prepare a callback to receive response/error of API call:

    `inner class SaveStudentCallback : ApiCallback<BaseResponse>() {`

        `override fun onResponse(response: BaseResponse?, networkResult: NetworkResult) {`
            `super.onResponse(response, networkResult)`
            `progressDialog.dismiss()`

            `if(response!!.status){`
                `Toast.makeText(context, R.string.saved_successfully, Toast.LENGTH_SHORT).show()`
            `}else{`
                `Toast.makeText(context, R.string.please_try_again, Toast.LENGTH_SHORT).show()`
            `}`
        `}`

        `override fun onErrorResponse(error: ApiError) {`
            `super.onErrorResponse(error)`
            `progressDialog.dismiss()`
            `Toast.makeText(context, R.string.please_try_again, Toast.LENGTH_SHORT).show()`
        `}`
    `}`

**Finally, POST request for saving Student object will look like:**
    
    `val student = Student()`
    `student.id = 1`
    `student.name = "Allen Solly"`

    `val saveStudentUrl = "http://urk_to_save_student" //TODO: edit url here`

    `val apiRequest = ApiRequest(ApiRequest.POST, saveStudentUrl, student, BaseResponse::class.java, callback)`
    `apiRequest.execute()`

**If you're coding in Java language then refer:**
[Using MyVolley with Java](https://github.com/rastogitech/MyVolley-Kotlin/wiki/Java:-How-to-use-MyVolley-for-Android)

**If you are coding in Kotlin language then refer:**
[Using MyVolley with Kotlin](https://github.com/rastogitech/MyVolley-Kotlin/wiki/Kotlin:-How-to-use-MyVolley-for-Android)
