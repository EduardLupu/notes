package com.example.notesappnative

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notesappnative.databinding.ActivityAddNoteBinding
import java.util.Date

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        binding.saveButton.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val content = binding.editTextContent.text.toString()
            val note = Note(
                id = 0,
                title = title,
                content = content,
                createdAt = Date().toString(),
                updatedAt = Date().toString()
            )

            val bundle = Bundle()
            bundle.putParcelable("note", note)
            intent.putExtra("noteBundle", bundle)
            setResult(Activity.RESULT_OK, intent)

            finish()
            Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}