package com.example.mvvm.note_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.note_app.data.model.NoteEntity
import com.example.mvvm.note_app.data.repository.MainRepository
import com.example.mvvm.note_app.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel(){

    var notesData = MutableLiveData<DataStatus<List<NoteEntity>>>()

    fun getAllNotes() = viewModelScope.launch {
        repository.allNotes().collect{
            notesData.postValue(DataStatus.success(it,it.isEmpty()))
        }
    }

    fun getSearchNotes(search:String) = viewModelScope.launch {
        repository.searchNotes(search).collect{
            notesData.postValue(DataStatus.success(it,it.isEmpty()))
        }
    }

    fun getFilterNotes(filter:String) = viewModelScope.launch {
        repository.filterNotes(filter).collect{
            notesData.postValue(DataStatus.success(it,it.isEmpty()))
        }
    }

    fun deleteNote(entity: NoteEntity) = viewModelScope.launch {
        repository.deleteNote(entity)
    }
}