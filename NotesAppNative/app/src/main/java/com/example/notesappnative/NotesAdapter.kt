package com.example.notesappnative

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope

class NotesAdapter(private var notes: List<Note>, private var context: MainActivity) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val idView: TextView = itemView.findViewById(R.id.noteIdTextView)
        val lastUpdatedTextView: TextView = itemView.findViewById(R.id.lastUpdateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.dateTextView.text = "Created: ${note.createdAt.toString().substring(0, 10)}"
        holder.idView.text = "#${note.id}"
        holder.lastUpdatedTextView.text = "Updated: ${note.updatedAt.toString().substring(0, 10)}"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("noteId", note.id)
            context.startActivityForResult(intent, 2)
        }
    }

    fun refreshNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}