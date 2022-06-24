package com.examples.moviesfeed.repository

import com.examples.moviesfeed.BuildConfig
import com.examples.moviesfeed.model.GetMoviesResponse
import com.examples.moviesfeed.model.Movie
import com.examples.moviesfeed.model.network.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor() {
    @Inject
    lateinit var api: Api
    private var apiKey = BuildConfig.NYT_API_KEY

    fun getMovies(
        offset: Int,
        onSuccess: (moviesList: List<Movie>) -> Unit,
        onError: (t: Throwable) -> Unit
    ) {
        api.getMoviesFromApi(offset, apiKey)
            .enqueue(object : Callback<GetMoviesResponse> {

                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {

                            onSuccess.invoke(responseBody.movies)
                        } else {
                            val t = Throwable("Empty set")
                            onError.invoke(t)
                        }
                    } else {
                        val t = Throwable("Request failed")
                        onError.invoke(t)
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke(t)
                }
            })
    }
}