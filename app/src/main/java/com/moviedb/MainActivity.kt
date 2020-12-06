package com.moviedb

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.castorflex.android.circularprogressbar.CircularProgressBar


class MainActivity : AppCompatActivity() {
    private val TAG:String="MainActivity"
    private var noResult: TextView? = null
    private lateinit var searchIcon: ImageView
    private var searchEdit: EditText? = null
    private var movieList: ArrayList<MovieData> ?= null
    private var movieRecycler: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager ?= null
    private var movieAdapter: MovieAdapter? = null
    private lateinit var movieViewModel : MovieViewModel
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var currentPageNo=1
    private var progressBar: CircularProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_main)
        if(savedInstanceState==null){
            searchEdit = findViewById(R.id.search_text)
            searchIcon = findViewById(R.id.search_icon)
            noResult = findViewById(R.id.no_results_text)
            progressBar=findViewById(R.id.loader)
            movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
            initializeGridRecyclerView()
            searchEdit?.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.length >= 3) {
                        if (Util.isOnline(this@MainActivity)) {
                            hideNoInternetMessage()
                            if (movieAdapter != null) {
                                movieAdapter?.initializeList()
                            }
                            currentPageNo = 1
                            showLoader()
                            hideNoResult()
                            movieViewModel.getMovieList(searchEdit?.text.toString(), currentPageNo)
                        } else {
                            showNoInternetMessage()
                        }
                    } else if (s.isEmpty()) {
                        hideNoResult()
                        hideNoInternetMessage()
                        movieAdapter?.initializeList()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }

    }

    private fun initializeGridRecyclerView() {

        try {
            movieRecycler = findViewById(R.id.movie_recycler)
            gridLayoutManager = GridLayoutManager(
                applicationContext,
                3,
                LinearLayoutManager.VERTICAL,
                false
            )
            movieRecycler?.layoutManager = gridLayoutManager
            movieRecycler?.setHasFixedSize(true)
            movieViewModel.movieList.observe(this, {
                try {
                    hideLoader()
                    if (movieAdapter == null) {
                        if (movieViewModel.movieList.value != null) {
                            hideNoResult()
                            movieRecycler!!.visibility = VISIBLE
                            movieAdapter = MovieAdapter(
                                applicationContext,
                                it as ArrayList<MovieData>
                            )
                            movieRecycler?.adapter = movieAdapter
                        } else {
                            movieRecycler!!.visibility = GONE
                            showNoResult()
                        }
                    } else {
                        isLoading = false
                        if (it != null) {
                            hideNoResult()
                            movieAdapter?.addData(it as ArrayList<MovieData>)
                        }else{
                            if(movieAdapter?.movieList?.size==0){
                                showNoResult()
                            }

                        }

                    }


                } catch (ex: Exception) {
                    Log.e(TAG, ex.message)
                }

            })
            movieRecycler?.addOnScrollListener(object :
                PaginationScrollListener(gridLayoutManager!!) {
                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun loadMoreItems() {
                    currentPageNo++
                    isLoading = true
                    //you have to call loadmore items to get more data
                    getMoreItems()
                }
            })

        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }
    fun getMoreItems() {
        if(Util.isOnline(this@MainActivity)){
            showLoader()
            movieViewModel.getMovieList(searchEdit?.text.toString(), currentPageNo)
        }else{
            Toast.makeText(this@MainActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieRecycler?.layoutManager = GridLayoutManager(this@MainActivity, 3)
        } else {
            movieRecycler?.layoutManager = GridLayoutManager(this@MainActivity, 5)
        }
    }
    fun showNoInternetMessage(){
        noResult?.visibility=View.VISIBLE
        noResult?.text=getString(R.string.no_internet)
    }
    fun hideNoInternetMessage(){
        noResult?.visibility=View.GONE
    }
    fun showNoResult(){
        noResult?.visibility=View.VISIBLE
        noResult?.text=getString(R.string.no_results)
    }
    fun hideNoResult(){
        noResult?.visibility=View.GONE
    }
    private fun showLoader() {
        try {
            if (progressBar?.visibility != VISIBLE) {
                progressBar?.visibility = VISIBLE
            }
        } catch (ex: java.lang.Exception) {
            Log.e(TAG,ex.message)
        }
    }

    private fun hideLoader() {
        try {
            if (progressBar?.visibility == VISIBLE) {
                progressBar?.visibility = GONE
            }
        } catch (ex: java.lang.Exception) {
           Log.e(TAG,ex.message)
        }
    }


}