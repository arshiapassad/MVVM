package com.example.mvvm.note_app.data.repository

import academy.nouri.s4_mvvm.note_app.data.database.NoteDao
import com.example.mvvm.note_app.data.model.NoteEntity
import javax.inject.Inject

class MainRepository @Inject constructor(private val dao: NoteDao){
    fun allNotes() = dao.getAllNotes()
    fun searchNotes(search: String) = dao.searchNote(search)
    fun filterNotes(filter: String) = dao.filetNote(filter)
    suspend fun deleteNote(entity: NoteEntity) = dao.deleteNote(entity)
}