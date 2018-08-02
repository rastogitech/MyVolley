This library makes easy to send network requests having Json data with java/kotlin objects directly without requiring any manual conversion from Java/Kotlin objects to json. You can send Multipart/form-data request too using this library. This library uses Volley library internally to send network requests.

**1. Add following two dependencies in your module's build.gradle file using:**

    dependencies {
        implementation 'com.myvolley:MyVolley-kotlin:1.0.4'
        implementation 'com.android.volley:volley:1.1.0'
        implementation 'com.google.code.gson:gson:2.8.2'
    }


Note that, if you face any error like: **More than one file was found with OS independent path 'META-INF/DEPENDENCIES'** then add following lines in you module's build.gradle file:

    android.packagingOptions {
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/NOTICE'
    }


**If you're coding in Java language then refer:**
[Using MyVolley with Java](https://github.com/rastogitech/MyVolley-Kotlin/wiki/Java:-How-to-use-MyVolley-for-Android)

**If you are coding in Kotlin language then refer:**
[Using MyVolley with Kotlin](https://github.com/rastogitech/MyVolley-Kotlin/wiki/Kotlin:-How-to-use-MyVolley-for-Android)
