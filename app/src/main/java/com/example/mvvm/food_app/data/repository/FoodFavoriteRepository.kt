package com.example.mvvm.food_app.data.repository

import academy.nouri.s4_mvvm.food_app.data.database.FoodDao
import javax.inject.Inject

class FoodFavoriteRepository @Inject constructor(private val dao: FoodDao) {
    fun foodsList() = dao.getAllFoods()
}