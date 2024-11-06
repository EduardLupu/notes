package com.example.notesappnative

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.notesappnative.databinding.ActivityUpdateBinding
import java.util.Date

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete this note?")

        builder.setPositiveButton("Yes") { _, _ ->
            deleteNote()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteNote() {
        val returnIntent = Intent()
        returnIntent.putExtra("operation", "Delete")
        returnIntent.putExtra("id", noteId)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
        Toast.makeText(this, "Note deleted successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        noteId = intent.getIntExtra("noteId", -1)
        if (noteId == -1) {
            finish()
        }
        val note = db.getNoteById(noteId)

        binding.updateEditTextTitle.setText(note.title)
        binding.updateEditTextContent.setText(note.content)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {
            val title = binding.updateEditTextTitle.text.toString()
            val content = binding.updateEditTextContent.text.toString()
            val note = Note(
                id = noteId,
                title = title,
                content = content,
                createdAt = Date().toString(),
                updatedAt = Date().toString()
            )
            val returnIntent = Intent()
            val bundle = Bundle()
            bundle.putParcelable("note", note)
            returnIntent.putExtra("operation", "Update")
            returnIntent.putExtra("updatedNoteBundle", bundle)
            returnIntent.putExtra("id", noteId)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
            Toast.makeText(this, "Note updated successfully!", Toast.LENGTH_SHORT).show()
        }

        binding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
}