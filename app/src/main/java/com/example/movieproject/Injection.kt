package com.example.movieproject

import android.content.Context
import com.example.movieproject.api.MovieApi
import com.example.movieproject.api.MovieService

object Injection {

    fun provideMovieApiDataSource(): MovieApi {
        return MovieService.movieApi
    }

    fun provideViewModelFactory(): ViewModelFactory {
        val dataSource = provideMovieApiDataSource()
        return ViewModelFactory(dataSource)
    }
}