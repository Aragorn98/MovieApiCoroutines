package com.example.movieproject.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.movieproject.Injection
import com.example.movieproject.MovieApiViewModel
import com.example.movieproject.R
import com.example.movieproject.ViewModelFactory
import com.example.movieproject.adapters.CreditsAdapter
import com.example.movieproject.adapters.LogosAdapter
import com.example.movieproject.adapters.OnMoreClickedListener
import com.example.movieproject.adapters.ParentAdapter
import com.example.movieproject.api.models.*
import com.example.movieproject.listeners.*
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_description.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDescription : AppCompatActivity(), MovieClickListener,
    OnMoreClickedListener {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MovieApiViewModel
    lateinit var movies: Result
    val list = mutableListOf<Result>()
    private val creditsAdapter by lazy { CreditsAdapter() }
    private val logosAdapter by lazy { LogosAdapter() }
    private val firebaseCloudstore by lazy { FirebaseFirestore.getInstance() }
    private val favourites by lazy { firebaseCloudstore.collection(FAVOURITES) }

    companion object {
        private const val FAVOURITES = "favourites"

        private const val ARG_ID = "movie_id"
        private const val ARG_MOVIE = "movie"
        lateinit var movieId: String
        lateinit var movie: Movie

        fun start(context: Context, id: String, movie: Movie) {
            context.startActivity(
                Intent(context,
                    MovieDescription::class.java).apply {
                    putExtra(ARG_ID, id)
                    putExtra(ARG_MOVIE, movie)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_description)

        viewModelFactory = Injection.provideViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieApiViewModel::class.java)
        initUI()
    }

    private fun initUI() {
        with(credits_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = creditsAdapter
        }
        with(logos_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = logosAdapter
        }
        movieId = intent.getStringExtra(ARG_ID)
        movie = intent.getParcelableExtra(ARG_MOVIE)

        loadTopCredits()

        addToFavourites.setOnClickListener{
            addToFavouriteMovies()
        }

        removeFromFavourites.setOnClickListener{
            deleteFromFvouriteMovies()
        }

        loadMovieDesc()
        loadRecommendedMovies()
        loadSimilarMovies()
        loadVideo()
    }

    fun loadTopCredits(){
        CoroutineScope(Dispatchers.IO).launch {
            val credits = viewModel.loadTopCredits(movieId)
            withContext(Dispatchers.Main){
                if(credits.isSuccessful){
                    var list = credits.body()!!.cast
                    list = list.take(3)
                    creditsAdapter.setCasts(list)
                } else{
                    Toast.makeText(this@MovieDescription, credits.message(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun addToFavouriteMovies(){
        favourites.document(movie.id.toString()).set(movie).addOnCompleteListener {
                task ->
            run {
                if (task.isSuccessful) {
                    Toast.makeText(this, R.string.success_message,
                        Toast.LENGTH_LONG).show()


                } else {
                    Toast.makeText(this, task.exception?.message,
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun deleteFromFvouriteMovies(){
        favourites.document(movie.id.toString()).delete().addOnSuccessListener {
            Log.d("argyn", "deleted")
        }.addOnFailureListener{
            Log.d("argyn", "not deleted")
        }
    }

    fun loadMovieDesc(){
        CoroutineScope(Dispatchers.IO).launch {
            val movieDetails = viewModel.loadMovieDesc(movieId)
            withContext(Dispatchers.Main){
                if(movieDetails.isSuccessful){
                    loadDetails(movieDetails.body()!!)
                    logosAdapter.setLogos(movieDetails.body()!!.production_companies)
                }
                else{
                    Toast.makeText(this@MovieDescription, movieDetails.message(), Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    fun loadRecommendedMovies(){
        CoroutineScope(Dispatchers.IO).launch {
            val recommendedMovies = viewModel.loadRecommendedMovies(movieId)
            withContext(Dispatchers.Main){
                if(recommendedMovies.isSuccessful){
                    loadMovies(recommendedMovies.body()!!)
                } else{
                    Toast.makeText(this@MovieDescription, recommendedMovies.message(), Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    fun loadSimilarMovies(){
        CoroutineScope(Dispatchers.IO).launch {
            val similarMovies = viewModel.loadSimilarMovies(movieId)
            withContext(Dispatchers.Main){
                if(similarMovies.isSuccessful){
                    loadMovies(similarMovies.body()!!)
                } else{
                    Toast.makeText(this@MovieDescription, similarMovies.message(), Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    fun loadVideo(){
        CoroutineScope(Dispatchers.IO).launch{
            val video = viewModel.loadVideos(movieId)
            withContext(Dispatchers.Main){
                if(video.isSuccessful){
                    movie_video.setOnClickListener {
                        var intent = YouTubeStandalonePlayer.createVideoIntent(this@MovieDescription, "AIzaSyB2K-Jq5IF_4GxOwROSrTBfEuISnLKe4nM",
                            video.body()!!.results[0].key)
                        startActivity(intent)
                    }
                } else{
                    Toast.makeText(this@MovieDescription, video.message(), Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    override fun onMovieClicked(movie: Movie) {
        start(this, movie.id.toString(), movie)
    }

    override fun onMoreClicked(result: Result) {
        MovieListActivity.start(this, movies)
    }

    private fun loadMovies(movies: Result) {
        list.add(movies)
        with(recommended_movies){
            layoutManager = LinearLayoutManager(this@MovieDescription, LinearLayout.VERTICAL, false)
            adapter =
                ParentAdapter(list, this@MovieDescription, this@MovieDescription)
        }
    }


    private fun loadDetails(movieDetails: MovieDetails) {
        Picasso.get().load("http://image.tmdb.org/t/p/w780" + movieDetails.poster_path).into(movie_poster)
        movie_title.text = movieDetails.title
        movie_overview.text = movieDetails.overview

    }
}
