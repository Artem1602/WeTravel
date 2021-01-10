package ua.pkk.wetravel.retrofit

import com.squareup.moshi.Json

data class UserProperty(
        @Json(name = "email") val email:String,
        @Json(name = "password") val password:String
)

