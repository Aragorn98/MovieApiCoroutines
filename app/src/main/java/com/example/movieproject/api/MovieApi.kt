package com.example.movieproject.api

import com.example.movieproject.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {
    @GET("movie/popular?api_key=71eb7fc9baa53fdc5920a373c657c65e&page=1")
    suspend fun getPopularMovies(@Query("language") lang: String): Response<Result>

    @GET("movie/top_rated?api_key=71eb7fc9baa53fdc5920a373c657c65e&page=1")
    suspend fun getTopRatedMovies(@Query("language") lang: String): Response<Result>

    @GET("movie/upcoming?api_key=71eb7fc9baa53fdc5920a373c657c65e&page=1")
    suspend fun getUpcomingMovies(@Query("language") lang: String): Response<Result>

    @GET("movie/now_playing?api_key=71eb7fc9baa53fdc5920a373c657c65e&page=1")
    suspend fun getNowPlayingMovies(@Query("language") lang: String): Response<Result>

    @GET("movie/{movie_id}/recommendations?api_key=71eb7fc9baa53fdc5920a373c657c65e&")
    suspend fun getRecommendedMovies(
        @Path("movie_id") id: String,
        @Query("language") lang: String): Response<Result>

    @GET("movie/{movie_id}/similar?api_key=71eb7fc9baa53fdc5920a373c657c65e&page=1")
    suspend fun getSimilarMovies(
        @Path("movie_id") id: String,
        @Query("language") lang: String): Response<Result>

    @GET("movie/{movie_id}/videos?api_key=71eb7fc9baa53fdc5920a373c657c65e&")
    suspend fun getVideos(
        @Path("movie_id") id: String,
        @Query("language") lang: String): Response<Video>

    @GET("movie/{movie_id}?api_key=71eb7fc9baa53fdc5920a373c657c65e&")
    suspend fun getMovieDesc(
        @Path("movie_id") id: String,
        @Query("language") lang: String): Response<MovieDetails>

    @GET("discover/movie?api_key=71eb7fc9baa53fdc5920a373c657c65e&")
    suspend fun getMoviesByGenre(
        @Query("with_genres") id: String,
        @Query("language") lang: String): Response<Result>

    @GET("movie/{movie_id}/credits?api_key=71eb7fc9baa53fdc5920a373c657c65e")
    suspend fun getTopCredits(
        @Path("movie_id") id: String,
        @Query("language") lang: String): Response<Credits>

    @GET("genre/movie/list?api_key=71eb7fc9baa53fdc5920a373c657c65e&")
    suspend fun getGenres(@Query("language") lang: String): Response<Genres>

    @GET("authentication/token/new?api_key=71eb7fc9baa53fdc5920a373c657c65e")
    suspend fun getRequestToken(): Response<RequestToken>

    @POST("authentication/session/new?api_key=71eb7fc9baa53fdc5920a373c657c65e")
    suspend fun createSession(@Body requestToken: RequestTokenClass): Response<CreateSession>

    @DELETE("authentication/session?api_key=71eb7fc9baa53fdc5920a373c657c65e")
    suspend fun deleteSession(@Body sessionId: SessionIdClass): Response<DeleteSession>
}