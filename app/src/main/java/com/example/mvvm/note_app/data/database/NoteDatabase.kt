package academy.nouri.s4_mvvm.note_app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvm.note_app.data.model.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}