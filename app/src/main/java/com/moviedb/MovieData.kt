package com.moviedb

data class SearchedMovies(val Search : ArrayList<MovieData>)
data class MovieData ( val imdbID : String , val Poster : String,val Title :String,val Year:String)
data class MovieDetail(val Title: String , val Poster : String , val Released : String , val Runtime : String , val Genre : String , val Plot : String , val Director : String , val Actors : String)