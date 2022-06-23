package com.examples.moviesfeed.repository

import com.examples.moviesfeed.BuildConfig
import com.examples.moviesfeed.model.GetMoviesResponse
import com.examples.moviesfeed.model.Movie
import com.examples.moviesfeed.model.network.Api
import com.examples.moviesfeed.utils.API_BASE_URL
import com.examples.moviesfeed.utils.HTTP_AGENT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class UserAgentInterceptor(private val userAgent: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originRequest: Request = chain.request()
        val requestWithUserAgent: Request = originRequest.newBuilder()
            .header("User-Agent", userAgent)
            .build()
        return chain.proceed(requestWithUserAgent)
    }
}

object MainRepository {
    private val api: Api
    private var okHttp: OkHttpClient
    private var nytApiKey = BuildConfig.NYT_API_KEY

    init {
        val uA = HTTP_AGENT
        okHttp = OkHttpClient.Builder().addInterceptor(UserAgentInterceptor(uA)).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
        api = retrofit.create(Api::class.java)
    }

    fun getMovies(
        offset: Int,
        onSuccess: (moviesList: List<Movie>) -> Unit,
        onError: (t: Throwable) -> Unit
    ) {
        api.getMoviesFromApi(offset, nytApiKey)
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