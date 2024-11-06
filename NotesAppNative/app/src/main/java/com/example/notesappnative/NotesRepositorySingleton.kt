import android.content.Context
import com.example.notesappnative.NotesRepository

object NotesRepositorySingleton {
    private var instance: NotesRepository? = null

    fun getInstance(context: Context): NotesRepository {
        return instance ?: synchronized(this) {
            instance ?: NotesRepository(context).also { instance = it }
        }
    }
}