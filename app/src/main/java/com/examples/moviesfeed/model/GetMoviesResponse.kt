package com.examples.moviesfeed.model

import com.google.gson.annotations.SerializedName

data class GetMoviesResponse
    (
    @SerializedName("results") val movies: List<Movie>
)