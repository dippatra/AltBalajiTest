package com.moviedb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

    @GET("?")
    fun getMovies(
        @Query("apikey") apiKey: String,
        @Query("s") s: String,
        @Query("page") page: Int
    ): Call<SearchedMovies>

    @GET("?")
    fun getMovieDetail(
        @Query("apikey") apiKey: String,
        @Query("i") i: String
    ): Call<MovieDetail>
}
