package com.example.movieproject.listeners

import com.example.movieproject.api.models.Movie

interface MovieClickListener {
    fun onMovieClicked(movie: Movie)
}