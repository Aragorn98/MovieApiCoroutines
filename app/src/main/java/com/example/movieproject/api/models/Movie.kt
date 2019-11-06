package com.example.movieproject.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(
    var type: String?,
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
) : Parcelable

@Parcelize
data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) : Parcelable {

    constructor(): this(
        false,
        "",
        emptyList<Int>(),
        0,
        "",
        "",
        "",
        0.0,
        "",
        "",
        "",
        false,
        0.0,
        0


    )
}

@Parcelize
data class MovieDetails(
    val adult: Boolean,
    val backdrop_path: String,
    val budget: Int,
    val genres: List<Genre>,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Long,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) : Parcelable

@Parcelize
data class Genre(
    val id: Int,
    val name: String
) : Parcelable

@Parcelize
data class ProductionCompany(
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
) : Parcelable

@Parcelize
data class ProductionCountry(
    val iso_3166_1: String,
    val name: String
) : Parcelable

@Parcelize
data class SpokenLanguage(
    val iso_639_1: String,
    val name: String
) : Parcelable

@Parcelize
data class Genres(
    val genres: List<Genre>
) : Parcelable

@Parcelize
data class Credits(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
) : Parcelable

@Parcelize
data class Cast(
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val gender: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val profile_path: String
) : Parcelable

@Parcelize
data class Crew(
    val credit_id: String,
    val department: String,
    val gender: Int,
    val id: Int,
    val job: String,
    val name: String,
    val profile_path: String
) : Parcelable

@Parcelize
data class Video(
    val id: Int,
    val results: List<Videos>
) : Parcelable
@Parcelize
data class Videos(
    val id: String,
    val iso_3166_1: String,
    val iso_639_1: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
) : Parcelable


@Parcelize
data class RequestToken(
    val expires_at: String,
    val request_token: String,
    val success: Boolean
) : Parcelable

@Parcelize
data class RequestTokenClass(
    val request_token: String
) : Parcelable

@Parcelize
data class SessionIdClass(
    val session_id: String
) : Parcelable

@Parcelize
data class CreateSession(
    val success: Boolean,
    val session_id: String
): Parcelable

@Parcelize
data class DeleteSession(
    val success: Boolean
): Parcelable
