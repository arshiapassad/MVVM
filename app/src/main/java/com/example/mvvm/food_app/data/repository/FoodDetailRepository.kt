package com.example.mvvm.food_app.data.repository

import academy.nouri.s4_mvvm.food_app.data.database.FoodDao
import academy.nouri.s4_mvvm.food_app.data.database.FoodEntity
import academy.nouri.s4_mvvm.food_app.data.model.ResponseFoodsList
import academy.nouri.s4_mvvm.food_app.data.server.ApiServices
import com.example.mvvm.food_app.utils.MyResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FoodDetailRepository @Inject constructor(private val api: ApiServices, private val dao: FoodDao) {

    suspend fun foodDetail(id: Int): Flow<MyResponse<ResponseFoodsList>>{
        return flow {
            emit(MyResponse.loading())
            when(api.foodDetail(id).code()){
                in 200..202 -> {
                    emit(MyResponse.success(api.foodDetail(id).body()))
                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun saveFood(entity: FoodEntity) = dao.saveFood(entity)
    suspend fun deleteFood(entity: FoodEntity) = dao.deleteFood(entity)
    fun existsFood(id:Int) = dao.existsFood(id)

}