package com.dicoding.kelana.data.api

import com.dicoding.kelana.data.response.DetailWisataResponse
import com.dicoding.kelana.data.response.LoginResponse
import com.dicoding.kelana.data.response.PreferenceWisataResponse
import com.dicoding.kelana.data.response.PreferenceWisataResponseItem
import com.dicoding.kelana.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun postLogin(
        @Body request: LoginRequest
    ) : Call<LoginResponse>

    @POST("auth/register")
    fun postRegister(
        @Body request: RegisterRequest
    ) : Call<RegisterResponse>

    @GET("recommendations")
    fun fetchWisata(
        @Query("category") category: String
    ) : Call<List<PreferenceWisataResponseItem>>

    @GET("indonesia_tourism/detail")
    fun getDetailWisata(
        @Query("place_id") placeId: String
    ) : Call<DetailWisataResponse>
}

data class LoginRequest(
    var email : String,
    var password: String
)

data class RegisterRequest(
    var email: String,
    val password: String,
    var username: String
)