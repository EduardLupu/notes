package com.example.notesappnative

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NotesServerAPI {
    private const val BASE_URL = " http://10.0.2.2:8080"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
    val retrofitService: NotesServerService by lazy {
        retrofit.create(NotesServerService::class.java)
    }
}