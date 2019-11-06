package com.example.movieproject.api

//import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.okhttp.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
object MovieService {

    private lateinit var interceptor: HttpLoggingInterceptor

    init {

        interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    }

    val ENDPOINT = "https://api.themoviedb.org/3/"

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl(ENDPOINT)
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client)
//        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val movieApi = retrofit.create(MovieApi::class.java)
}

