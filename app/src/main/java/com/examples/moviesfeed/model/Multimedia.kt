package com.examples.moviesfeed.model

import com.google.gson.annotations.SerializedName

data class Multimedia(
    @SerializedName("type") val type: String,
    @SerializedName("src") val posterPath: String,
    @SerializedName("height") val posterHeight: Int,
    @SerializedName("width") val posterWidth: Int
)