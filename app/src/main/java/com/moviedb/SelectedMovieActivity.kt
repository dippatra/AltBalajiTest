package com.moviedb

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.moviedb.databinding.SelectedMovieActivityBinding

class SelectedMovieActivity : AppCompatActivity() {

    private lateinit var binding : SelectedMovieActivityBinding
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        binding = DataBindingUtil.setContentView(this,R.layout.selected_movie_activity)
        val movieID = intent.getStringExtra("imdbID")
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        movieViewModel.getMovieDetail(movieID)
        movieViewModel.movieDetail.observe(this,{
            if(movieViewModel.movieDetail.value != null){
                binding.setVariable(BR.movieViewModel, movieViewModel.movieDetail.value)
                binding.executePendingBindings()
                Glide.with(this)
                    .load(movieViewModel.movieDetail.value!!.Poster)
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.place_holder)
                    .into(binding.moviePoster)
            }
        })

        binding.backButton.setOnClickListener{
            onBackPressed()
        }
    }
}
