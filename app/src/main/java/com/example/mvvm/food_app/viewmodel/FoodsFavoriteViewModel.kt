package com.example.mvvm.food_app.viewmodel

import academy.nouri.s4_mvvm.food_app.data.database.FoodEntity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.food_app.data.repository.FoodFavoriteRepository
import com.example.mvvm.note_app.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodsFavoriteViewModel @Inject constructor(private val repository: FoodFavoriteRepository): ViewModel(){

    val favoriteListData = MutableLiveData<DataStatus<List<FoodEntity>>>()
    fun loadFavorites() = viewModelScope.launch(Dispatchers.IO){
        repository.foodsList().collect{
            favoriteListData.postValue(DataStatus.success(it, it.isEmpty()))
        }
    }
}