package com.example.notesappnative

import android.content.Context
import android.util.Log

class NotesRepository(context: Context) {
    private val db: NotesDatabaseHelper = NotesDatabaseHelper(context)
    private var notesList = mutableListOf<Note>()

    init {
        notesList = db.getAllNotes().toMutableList()
        printNotesLog()
    }

    fun getAllNotes(): List<Note> {
        return notesList
        printNotesLog()
    }

    fun addNote(note: Note): Int {
        var t = db.insertNote(note)
        notesList.add(t)
        printNotesLog()
        return t.id
    }

    fun updateNote(note: Note): Int {
        db.updateNote(note)
        val index = notesList.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notesList[index] = note
        }
        printNotesLog()
        return index
    }

    fun deleteNote(noteId: Int): Int {
        db.deleteNoteById(noteId)
        val index = notesList.indexOfFirst { it.id == noteId }
        notesList.removeIf { it.id == noteId }
        printNotesLog()
        return index
    }

    fun findNote(noteId: Int): Int {
        val index = notesList.indexOfFirst { it.id == noteId }
        notesList.removeIf { it.id == noteId }
        printNotesLog()
        return index
    }

    fun getNoteById(noteId: Int): Note {
        return notesList.first { it.id == noteId }
    }

    private fun printNotesLog() {
        notesList.forEach {
            Log.d("NotesRepo Log:", "Note ID: ${it.id}, Title: ${it.title}, Content: ${it.content}, CreatedAd: ${it.createdAt}, UpdatedAt: ${it.updatedAt}")
        }
    }
}