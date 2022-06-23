package com.examples.moviesfeed.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("display_title") val title: String,
    @SerializedName("summary_short") val overview: String,
    @SerializedName("publication_date") val releaseDate: String,
    @SerializedName("multimedia") val multimedia: Multimedia
)