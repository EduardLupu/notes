package com.example.notesappnative

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesappnative.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesRepository: NotesRepository
    private lateinit var notesAdapter: NotesAdapter
    private var context = this;

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        notesRepository = NotesRepositorySingleton.getInstance(context = this)

        lifecycleScope.launch {
            notesAdapter = NotesAdapter(notesRepository.getAllNotes(), context)
            binding.notesRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.notesRecyclerView.adapter = notesAdapter
        }

        if (!isNetworkConnected()) {
            showDialog()
        }

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                lifecycleScope.launch {
                    NotesServerAPI.retrofitService.retrieveAllNotes()
                        .enqueue(object : Callback<List<Note>?> {
                            override fun onResponse(
                                call: Call<List<Note>?>,
                                response: Response<List<Note>?>
                            ) {

                                val notesServer = response.body()!!
                                val noteDatabase = notesRepository.getAllNotes();

                                for (t1: Note in noteDatabase) {
                                    var exists = false
                                    for (t2: Note in notesServer) {
                                        if (t1.id!!.equals(t2.id)) {
                                            exists = true
                                        }
                                    }

                                    if (!exists) {
                                        NotesServerAPI.retrofitService.createNote(t1)
                                            .enqueue(object : Callback<Note?> {
                                                override fun onResponse(
                                                    call: Call<Note?>,
                                                    response: Response<Note?>
                                                ) {
                                                    Log.d(
                                                        "Added item from local db",
                                                        "Success: " + t1
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<Note?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to add item from local db!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Added item from local db",
                                                        "Failed: " + t.message
                                                    )
                                                }
                                            })
                                    }

                                }


                                for (t2: Note in notesServer) {
                                    var exists = false
                                    for (s1: Note in noteDatabase) {
                                        if (s1.id!!.equals(t2.id)) {
                                            exists = true
                                        }
                                    }

                                    if (!exists) {
                                        NotesServerAPI.retrofitService.deleteNotes(t2.id.toLong()!!)
                                            .enqueue(object : Callback<Note?> {
                                                override fun onResponse(
                                                    call: Call<Note?>,
                                                    response: Response<Note?>
                                                ) {

//                                                stories.remove(t2)
                                                    Log.d(
                                                        "Deleted item from server",
                                                        "Success!"
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<Note?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to remove item from server!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Deleted item from server",
                                                        "Failed! " + t.message
                                                    )
                                                }
                                            })
                                    }
                                }


                                for (t1: Note in noteDatabase) {
                                    var different = false
                                    for (t2: Note in notesServer) {
                                        if (t1.id!!.equals(t2.id) && (!t1.title.equals(t2.title) || !t1.content.equals(
                                                t2.content
                                            ) || !t1.createdAt.equals(t2.createdAt) || !t1.updatedAt.equals(
                                                t2.updatedAt
                                            ))
                                        ) {
                                            different = true
                                            Log.d(
                                                "Updated item from the server",
                                                "Success: " + t2
                                            )
                                        }
                                    }

                                    if (different) {
                                        NotesServerAPI.retrofitService.updateNote(t1.id.toLong()!!, t1)
                                            .enqueue(object : Callback<Note?> {
                                                override fun onResponse(
                                                    call: Call<Note?>,
                                                    response: Response<Note?>
                                                ) {
                                                    Log.d(
                                                        "Updated item from the server",
                                                        "Success: " + t1
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<Note?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to update item from server!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Updated item from the server",
                                                        "Failed: " + t.message
                                                    )
                                                }
                                            })
                                    }

                                }
                            }

                            override fun onFailure(call: Call<List<Note>?>, t: Throwable) {
                                lifecycleScope.launch {
                                    Toast.makeText(
                                        context,
                                        "Failed to check differences between local db and server!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d(
                                        "Check for differences between local db and server",
                                        "Failed: " + t.message
                                    )
                                }
                            }
                        })
                }
            }
        }
        )

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST_CODE)
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var newNotes = db.getAllNotes(
                    searchQuery = p0.toString()
                )
                notesAdapter.refreshNotes(newNotes = newNotes)
                binding.noNotesTextView.text = "${newNotes.size} notes"
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.noNotesTextView.text = "${db.getAllNotesCount()} notes"
    }

    fun deleteNote (id: Long) {
        notesRepository.deleteNote(id.toInt())

        if (isNetworkConnected()) {
            lifecycleScope.launch {
                NotesServerAPI.retrofitService.deleteNotes(id).enqueue(object : Callback<Note?> {
                    override fun onResponse(call: Call<Note?>, response: Response<Note?>) {
                        notesAdapter.notifyDataSetChanged()
                        Log.d("Delete note action - server", "Success: " + response.body())
                    }

                    override fun onFailure(call: Call<Note?>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Failed to delete note" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Delete note action - server", "Failed: " + t.message)
                    }
                })
            }

        } else {
            showDialog()
            notesRepository.deleteNote(id.toInt())
            Log.d("Delete note action - local database", "Success!")
        }
        notesAdapter.notifyDataSetChanged()

    }


    fun addNote(note: Note) {

        var noteId = notesRepository.addNote(note)

        note.id = noteId

        if (isNetworkConnected()) {
            lifecycleScope.launch {

                NotesServerAPI.retrofitService.createNote(note).enqueue(object : Callback<Note?> {
                    override fun onResponse(call: Call<Note?>, response: Response<Note?>) {
                        notesAdapter.notifyDataSetChanged()
                        Log.d("Add note action - server", "Success: " + response.body().toString())
                    }

                    override fun onFailure(call: Call<Note?>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Failed to add note!" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Add note action - server", "Failed: " + t.message)
                    }
                })
            }
        } else {
            showDialog()
            notesAdapter.notifyDataSetChanged()
            Log.d("Add note action - local database", "Success!")
        }
    }


    fun updateNote (note: Note, id: Long) {

        notesRepository.updateNote(note)

        if (isNetworkConnected()) {
            lifecycleScope.launch {
                NotesServerAPI.retrofitService.updateNote(id, note).enqueue(object : Callback<Note?> {
                    override fun onResponse(call: Call<Note?>, response: Response<Note?>) {
                        notesAdapter.notifyDataSetChanged()
                        Log.d("Update note action - server", "Success: " + response.body())
                    }

                    override fun onFailure(call: Call<Note?>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Failed to update note!" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Update note action - server", "Failed: " + t.message)
                    }
                })
            }
        } else {
            showDialog()
            notesRepository.updateNote(note)
            notesAdapter.notifyDataSetChanged()
            Log.d("Update note action - local database", "Success!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val bundle = data.getBundleExtra("noteBundle")
                val note = bundle?.getParcelable<Note>("note")
                if (note != null) {
                    addNote(note)
                    val position = notesAdapter.itemCount - 1
                    notesAdapter.notifyItemInserted(position)
                    Toast.makeText(this, "Note added!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (requestCode == UPDATEDELETE_NOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                if (data.getStringExtra("operation") == "Update") {
                    val bundle = data.getBundleExtra("updatedNoteBundle")
                    val task = bundle?.getParcelable<Note>("note")
                    val id = data.getIntExtra("id", -1)
                    if (task != null && id != -1) {
                        updateNote(task, task.id.toLong())
                        notesAdapter.notifyDataSetChanged()
                        Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show()

                    }
                }
                else if (data.getStringExtra("operation") == "Delete") {
                    val id = data.getIntExtra("id", -1)
                    if (id != -1) {
                        deleteNote(id.toLong())
                        notesAdapter.notifyDataSetChanged()
                        Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        binding.noNotesTextView.text = "${db.getAllNotesCount()} notes"
        notesAdapter.refreshNotes(
            newNotes = db.getAllNotes()
        )
    }

    private fun showDialog() {
        AlertDialog.Builder(this).setTitle("No Internet Connection")
            .setMessage("Fallback on local DB")
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    companion object {
        private const val ADD_NOTE_REQUEST_CODE = 1
        private const val UPDATEDELETE_NOTE_REQUEST_CODE = 2
    }
}