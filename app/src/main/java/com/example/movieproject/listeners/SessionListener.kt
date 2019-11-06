package com.example.movieproject.listeners

import com.example.movieproject.api.models.CreateSession

interface SessionListener {
    fun onSessionCreated(session: CreateSession)
    fun onSessionCreateError(throwable: Throwable)
}