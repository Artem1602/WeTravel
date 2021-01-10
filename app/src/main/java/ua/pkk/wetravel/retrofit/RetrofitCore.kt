package ua.pkk.wetravel.retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_URL = "https://wetravel-1591a.firebaseio.com/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

interface UserApiService {
    //user_data
    @GET("user_data.json")
    fun getAllUserData(): Call<Map<String, UserData>>

    @GET("user_data/{id}.json")
    fun getUserData(@Path("id") id: String): Call<UserData>

    @PUT("user_data.json")
    fun createNewUserData(@Body data: Map<String, UserData>): Call<Map<String, UserData>>

    //users
    @GET("users.json")
    fun getAllUsers(): Call<Map<String, UserProperty>>

    @GET("users/{id}.json")
    fun getUser(@Path("id") id: String): Call<UserProperty>

    @PUT("users.json")
    fun createNewUser(@Body user: Map<String, UserProperty>): Call<Map<String, UserProperty>>
}

object UserAPI {
    val RETROFIT_SERVICE: UserApiService by lazy { retrofit.create(UserApiService::class.java) }
}