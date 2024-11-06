package com.example.notesappnative

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_CREATED_AT = "created_at"
        private const val COLUMN_UPDATED_AT = "updated_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSql =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_CREATED_AT DATETIME, $COLUMN_UPDATED_AT DATETIME);"
        db.execSQL(createTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableSql = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableSql)
        onCreate(db!!)
    }

    fun insertNote(note: Note): Note {
        val db = writableDatabase
        val currentTimestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_CREATED_AT, currentTimestamp)
            put(COLUMN_UPDATED_AT, currentTimestamp)
        }
        val id = db.insert(TABLE_NAME, null, values)
        val newNote = Note(id.toInt(), note.title, note.content, currentTimestamp, currentTimestamp)
        db.close()
        return newNote
    }

    fun updateNote(note: Note) {
        val db = writableDatabase
        val currentTimestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_UPDATED_AT, currentTimestamp)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(note.id.toString()))
        db.close()
    }

    fun getNoteById(id: Int): Note {
        val db = readableDatabase
        val selectNoteByIdQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(selectNoteByIdQuery, arrayOf(id.toString()))
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT))
        val updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT))
        val note = Note(
            id = id,
            title = title,
            content = content,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
        cursor.close()
        db.close()
        return note
    }

    fun getAllNotes(searchQuery: String? = null): List<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        var selectAllNotesQuery = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_UPDATED_AT DESC"

        if (!searchQuery.isNullOrBlank()) {
            selectAllNotesQuery =
                "SELECT * FROM $TABLE_NAME WHERE $COLUMN_TITLE LIKE ? ORDER BY $COLUMN_UPDATED_AT DESC"
        }

        val cursor = if (!searchQuery.isNullOrBlank()) {
            db.rawQuery(selectAllNotesQuery, arrayOf("%$searchQuery%"))
        } else {
            db.rawQuery(selectAllNotesQuery, null)
        }

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT))
            val updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT))
            val note = Note(
                id = id,
                title = title,
                content = content,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }

    fun getAllNotesCount(): Int {
        val db = readableDatabase
        val selectAllNotesQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectAllNotesQuery, null)
        val count = cursor.count
        cursor.close()
        db.close()
        return count
    }

    fun deleteNoteById(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}