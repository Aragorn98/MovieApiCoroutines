package com.example.movieproject.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import com.example.movieproject.*
import com.example.movieproject.adapters.OnMoreClickedListener
import com.example.movieproject.adapters.ParentAdapter
import com.example.movieproject.api.MovieService
import com.example.movieproject.api.models.*
import com.example.movieproject.api.models.DeleteSession
import com.example.movieproject.listeners.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(),  MovieClickListener,
    OnMoreClickedListener, AdapterView.OnItemSelectedListener{

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MovieApiViewModel
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    val list = mutableListOf<Result>()
    private lateinit var requestToken: String
    var sessionId: String? = null
    val genreName = mutableListOf<String>()
    lateinit var genres: List<Genre>
    private val firebaseCloudstore by lazy { FirebaseFirestore.getInstance() }
    private val favourites by lazy { firebaseCloudstore.collection(FAVOURITES) }
    private var check = 0
    companion object {
        private const val FAVOURITES = "favourites"
        fun start(context: Context) {
            context.startActivity(
                Intent(context,
                    MainActivity::class.java)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModelFactory = Injection.provideViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieApiViewModel::class.java)

        val user = firebaseAuth.currentUser

        if (user == null) {
            LoginActivity.start(this)
            finish()
        }


        createRequestToken.setOnClickListener{
            createRequestToken()
        }

        createSession.setOnClickListener{
            createSession()
        }

        deleteSession.setOnClickListener{
            deleteSession()
        }

        eng.setOnClickListener {
            setLang("en")
        }
        rus.setOnClickListener {
            setLang("ru")
        }
        favourite_movies.setOnClickListener {
            showFavouriteMovies()
        }

        genres_spinner.setSelection(0, false)
        loadGenres()


        CoroutineScope(Dispatchers.Main).launch{
            loadTopRatedMovies()
            loadPopularMovies()
            loadUpcomingMovies()
            loadNowPlayingMovies()
        }

    }


    private fun createRequestToken(){
        CoroutineScope(Dispatchers.IO).launch {
            val reqToken = viewModel.loadRequestToken()
            withContext(Dispatchers.Main){
                if(reqToken.isSuccessful){
                    requestToken = reqToken.body()!!.request_token
                    val url = "https://www.themoviedb.org/authenticate/$requestToken"
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } else{
                    Toast.makeText(this@MainActivity, reqToken.message(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createSession(){
        CoroutineScope(Dispatchers.IO).launch {
            val createSession = viewModel.createSession(RequestTokenClass(requestToken))
            withContext(Dispatchers.Main){
                if(createSession.isSuccessful){
                    sessionId = createSession.body()!!.session_id
                    Toast.makeText(this@MainActivity, "Session created", Toast.LENGTH_LONG).show()
                } else{
                    Toast.makeText(this@MainActivity, createSession.message(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deleteSession(){
        if(sessionId != null){
            CoroutineScope(Dispatchers.IO).launch {
                val deleteSession = viewModel.deleteSession(SessionIdClass(sessionId.toString()))
                withContext(Dispatchers.Main){
                    if(deleteSession.isSuccessful){
                        Toast.makeText(this@MainActivity, deleteSession.body()!!.success.toString(), Toast.LENGTH_LONG).show()
                    } else{
                        Toast.makeText(this@MainActivity, deleteSession.message(), Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
        else Toast.makeText(this, "There is no session", Toast.LENGTH_LONG).show()
    }

    private fun setLang(eng: String){
        LangPref.lang = eng
        Log.d("agryn", eng)
    }

    private fun showFavouriteMovies(){
        var movies = ArrayList<Movie>()
        favourites.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    document.forEach { snapshot ->
                        Log.d("argyn", snapshot.toString())
                        movies.add(snapshot.toObject(Movie::class.java))
                    }


                    val result = Result("Favourites", 0, movies, 0, 0)

                    Log.d("argyn", result.toString())
                    Log.d("argyn", movies.toString())
                    MovieListActivity.start(this, result)

                    Log.d("argyn", " not null")
                } else {
                    Log.d("argyn", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("argyn", "get failed with ", exception)
            }
    }

    private fun loadGenres(){
        CoroutineScope(Dispatchers.IO).launch {
            val gen = viewModel.loadGenres()
            withContext(Dispatchers.Main){
                if (gen.isSuccessful) {
                    genres = gen.body()!!.genres
                    for (genre: Genre in genres){
                        genreName.add(genre.name)
                    }
                    val arrayAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, genreName)
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    genres_spinner!!.adapter = arrayAdapter
                    genres_spinner.onItemSelectedListener = this@MainActivity
                } else {
                    Toast.makeText(this@MainActivity, gen.message(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private suspend fun loadTopRatedMovies(){
        val topRatedMovies = viewModel.loadTopRatedMovies()
        if(topRatedMovies.isSuccessful){
            Log.d("argyn", topRatedMovies.body()!!.toString())
            list.add(topRatedMovies.body()!!)
        } else{
            Toast.makeText(this@MainActivity, topRatedMovies.message(), Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun loadPopularMovies(){
        val popularMovies = viewModel.loadPopularMovies()
        if(popularMovies.isSuccessful){
            list.add(popularMovies.body()!!)
        } else{
            Toast.makeText(this@MainActivity, popularMovies.message(), Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun loadUpcomingMovies(){
        val upcomingMovies = viewModel.loadUpcomingMovies()
        if(upcomingMovies.isSuccessful){
            list.add(upcomingMovies.body()!!)
        } else{
            Toast.makeText(this@MainActivity, upcomingMovies.message(), Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun loadNowPlayingMovies(){
        val nowPlayingMovies = viewModel.loadNowPlayingMovies()
        if(nowPlayingMovies.isSuccessful){
            list.add(nowPlayingMovies.body()!!)
            onMoviesChanged(list)
        } else{
            Toast.makeText(this@MainActivity, nowPlayingMovies.message(), Toast.LENGTH_LONG).show()
        }
    }

    private fun onMoviesChanged(list: List<Result>) {
        Log.d("argyn", list.toString())
        Log.d("argyn", list.size.toString())
        with(rv_parent){
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.VERTICAL, false)
            adapter = ParentAdapter(list, this@MainActivity, this@MainActivity)
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(++check > 1){
            CoroutineScope(Dispatchers.IO).launch {
                val result = viewModel.loadMoviesByGenre(genres[position].id.toString(), genres[position].name)
                withContext(Dispatchers.Main){
                    if(result.isSuccessful){
                        MovieListActivity.start(this@MainActivity, result.body()!!)
                    } else{
                        Toast.makeText(this@MainActivity, result.message(), Toast.LENGTH_LONG).show()
                    }

                }
            }
        }

    }


    override fun onMoreClicked(result: Result) {
        MovieListActivity.start(this, result)
    }

    override fun onMovieClicked(movie: Movie) {
        Log.d("argyn", movie.id.toString())
        MovieDescription.start(this, movie.id.toString(), movie)
    }
}
