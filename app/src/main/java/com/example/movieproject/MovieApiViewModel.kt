package com.example.movieproject

import android.arch.lifecycle.ViewModel
import com.example.movieproject.api.MovieApi
import com.example.movieproject.api.models.*
import retrofit2.Response

class MovieApiViewModel(private val dataSource: MovieApi) : ViewModel() {
    var moviesListType: String = "ee"


    suspend fun loadGenres(): Response<Genres>{
        return dataSource.getGenres(LangPref.lang)
    }

    suspend fun loadPopularMovies(): Response<Result> {
        moviesListType = "Popular"
        return dataSource.getPopularMovies(LangPref.lang)
    }

    suspend fun loadTopRatedMovies(): Response<Result> {
        moviesListType = "Top Rated"
        return dataSource.getTopRatedMovies(LangPref.lang)
    }

    suspend fun loadUpcomingMovies(): Response<Result> {
        moviesListType = "Upcoming"
        return dataSource.getUpcomingMovies(LangPref.lang)
    }

    suspend fun loadNowPlayingMovies(): Response<Result> {
        moviesListType = "Now Playing"
        return dataSource.getNowPlayingMovies(LangPref.lang)
    }

    suspend fun loadRecommendedMovies(id: String): Response<Result> {
        moviesListType = "Recommended Movies"
        return dataSource.getRecommendedMovies(id, LangPref.lang)
    }

    suspend fun loadSimilarMovies(id: String): Response<Result> {
        moviesListType = "Similar movies"
        return dataSource.getSimilarMovies(id,LangPref.lang)
    }

    suspend fun loadMoviesByGenre(id: String, name: String): Response<Result> {
        moviesListType = name
        return dataSource.getMoviesByGenre(id, LangPref.lang)
    }

    suspend fun createSession(requestToken: RequestTokenClass): Response<CreateSession> {
        return dataSource.createSession(requestToken)
    }

    suspend fun loadTopCredits(id: String): Response<Credits> {
        return dataSource.getTopCredits(id, LangPref.lang)
    }

    suspend fun deleteSession(sessionId: SessionIdClass): Response<DeleteSession> {
        return dataSource.deleteSession(sessionId)
    }
    suspend fun loadMovieDesc(id: String): Response<MovieDetails> {
        return dataSource.getMovieDesc(id, LangPref.lang)
    }
    suspend fun loadRequestToken(): Response<RequestToken> {
        return dataSource.getRequestToken()
    }
    suspend fun loadVideos(id: String): Response<Video> {
        return dataSource.getVideos(id, LangPref.lang)
    }

}
