package com.examples.moviesfeed.model.network

import com.examples.moviesfeed.model.GetMoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("reviews/all.json")
    fun getMoviesFromApi(
        @Query("offset") page: Int,
        @Query("api-key") apiKey: String
    ): Call<GetMoviesResponse>
}