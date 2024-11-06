package com.example.notesappnative

import retrofit2.Call
import retrofit2.http.*

interface NotesServerService {
    @GET("/notes/{id}")
    fun retrieveNote(@Path("id") id: Int) : Call<Note>
    @GET("/notes")
    fun retrieveAllNotes() : Call<List<Note>>
    @DELETE("/notes/{id}")
    fun deleteNotes(@Path("id") id: Long) : Call<Note>
    @POST("/notes")
    fun createNote(@Body note: Note) : Call<Note>
    @PUT("/notes/{id}")
    fun updateNote(@Path("id") id: Long, @Body note: Note) : Call<Note>
}