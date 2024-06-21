package com.dicoding.kelana.data.api

import com.dicoding.kelana.data.response.DistanceMatrixResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsService {
    @GET("distancematrix/json")
    fun getDistance(
        @Query("origins") origins: String,
        @Query("destinations") destinations: String,
        @Query("key") apiKey: String
    ): Call<DistanceMatrixResponse>
}