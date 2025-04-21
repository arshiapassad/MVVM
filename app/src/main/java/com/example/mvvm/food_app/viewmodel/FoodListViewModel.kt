package com.example.mvvm.food_app.viewmodel


import academy.nouri.s4_mvvm.food_app.data.model.ResponseCategoriesList
import academy.nouri.s4_mvvm.food_app.data.model.ResponseFoodsList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.food_app.data.repository.FoodListRepository
import com.example.mvvm.food_app.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FoodListViewModel @Inject constructor(private val repository: FoodListRepository) : ViewModel(){

    init {
        loadFoodRandom()
        loadCategoriesList()
    }

    val randomFoodData = MutableLiveData<List<ResponseFoodsList.Meal>>()
    fun loadFoodRandom() = viewModelScope.launch(Dispatchers.IO) {
        repository.randomFood().collect {
            randomFoodData.postValue(it.body()?.meals!!)
        }
    }

    val filtersListData = MutableLiveData<MutableList<Char>>()
    fun loadFilterList() = viewModelScope.launch(Dispatchers.IO){
        val letters = listOf('A'..'Z').flatten().toMutableList()
        filtersListData.postValue(letters)
    }

    val categoriesListData = MutableLiveData<MyResponse<ResponseCategoriesList>>()
    fun loadCategoriesList() = viewModelScope.launch(Dispatchers.IO){
        repository.categoriesList().collect{ categoriesListData.postValue(it) }
    }

    val foodsListData = MutableLiveData<MyResponse<ResponseFoodsList>>()
    fun loadFoodsList(letter: String) = viewModelScope.launch(Dispatchers.IO){
        repository.foodList(letter).collect{ foodsListData.postValue(it) }
    }

    fun loadFoodBySearch(letter: String) = viewModelScope.launch(Dispatchers.IO){
        repository.foodBySearch(letter).collect{ foodsListData.postValue(it) }
    }

    fun loadFoodByCategory(letter: String) = viewModelScope.launch(Dispatchers.IO){
        repository.foodByCategory(letter).collect{ foodsListData.postValue(it) }
    }

}