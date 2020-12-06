package com.moviedb

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieViewModel : ViewModel() {

    companion object {

        var BASE_URL = "http://www.omdbapi.com/"
        var API_KEY = "e5311742"
        //var searchText = "Batman"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MovieAPI::class.java)
    }

    val movieList: MutableLiveData<ArrayList<MovieData>> = MutableLiveData()
    val movieDetail : MutableLiveData<MovieDetail> = MutableLiveData()

    fun getMovieList(searchText: String, page:Int)  {
        val call = service.getMovies(API_KEY, searchText, page)

        call.enqueue(object : Callback<SearchedMovies>{
            override fun onResponse(call: Call<SearchedMovies>, response: Response<SearchedMovies>) {
                val movies = response.body()!!
                movieList.value = movies.Search
            }

            override fun onFailure(call: Call<SearchedMovies>, t: Throwable) {
                t.message
            }
        })
    }

    fun getMovieDetail(movieID: String?) {
        val call = service.getMovieDetail(API_KEY, movieID!!)
        call.enqueue(object : Callback<MovieDetail>{
            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                val movieData = response.body()
                movieDetail.value = movieData
            }

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                t.message
            }

        })
    }
}