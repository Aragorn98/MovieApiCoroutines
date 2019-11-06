package com.example.movieproject.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.movieproject.R
import com.example.movieproject.adapters.MoviesAdapter
import com.example.movieproject.api.models.Movie
import com.example.movieproject.api.models.Result
import com.example.movieproject.listeners.MovieClickListener
import kotlinx.android.synthetic.main.activity_movie_list.*

class MovieListActivity : AppCompatActivity(), MovieClickListener {

    private val moviesAdapter by lazy { MoviesAdapter() }
    lateinit var movies: Result

    companion object {
        private const val ARG_ID = "movies"

        fun start(context: Context, list: Result) {
            context.startActivity(
                Intent(context,
                    MovieListActivity::class.java).apply {
                    putExtra(ARG_ID, list)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        initUI()
    }

    private fun initUI() {
        moviesAdapter.setListener(this)

        with(movies_list) {
            layoutManager = android.support.v7.widget.LinearLayoutManager(context)
            adapter = moviesAdapter
        }
        movies = intent.getParcelableExtra(ARG_ID)
        loadMovieList(movies)
    }

    private fun loadMovieList(movies: Result) {
        moviesAdapter.setMovies(movies.results)
    }

    override fun onMovieClicked(movie: Movie) {
        MovieDescription.start(this, movie.id.toString(), movie)
    }
}
