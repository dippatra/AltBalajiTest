package com.moviedb

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviedb.databinding.MovieGridItemBinding
import com.squareup.picasso.Picasso

class MovieAdapter(var context: Context, var movieList : ArrayList<MovieData>) :
    RecyclerView.Adapter<MovieAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieGridItemBinding.inflate(layoutInflater)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(movieList[position])
        val url: String = movieList[position].Poster
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.place_holder)
            .error(R.drawable.place_holder)
            .into(holder.binding.movieThumbnail)

        holder.binding.movieThumbnail.setOnClickListener{
            try{
                val intent = Intent(context,SelectedMovieActivity::class.java)
                intent.putExtra("imdbID",movieList[position].imdbID)
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }catch(ex : Exception){
                ex.printStackTrace()
            }
        }
    }

    class ItemHolder(val binding : MovieGridItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: MovieData){
            binding.setVariable(BR.mainViewModel, data)
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
    fun addData(listItems: ArrayList<MovieData>) {
        var size = this.movieList.size
        this.movieList.addAll(listItems)
        if(size==0&&listItems.size>0){
            notifyDataSetChanged()
            return
        }
        var sizeNew = this.movieList.size
         notifyItemRangeInserted(size, sizeNew)
    }
    fun initializeList(){
        this.movieList.clear()
        notifyDataSetChanged()
    }
}
