package com.example.mvvm.food_app.data.repository


import academy.nouri.s4_mvvm.food_app.data.model.ResponseCategoriesList
import academy.nouri.s4_mvvm.food_app.data.model.ResponseFoodsList
import academy.nouri.s4_mvvm.food_app.data.server.ApiServices
import com.example.mvvm.food_app.utils.MyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject


class FoodListRepository @Inject constructor(private val api: ApiServices) {

    suspend fun randomFood(): Flow<Response<ResponseFoodsList>> {
        return flow {
            emit(api.foodRandom())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun categoriesList(): Flow<MyResponse<ResponseCategoriesList>>{
        return flow {
            emit(MyResponse.loading())
            //Response
            when(api.categoriesList().code()){
                in 200..202 -> {
                    emit(MyResponse.success(api.categoriesList().body()))
                }
                422 -> {
                    emit(MyResponse.error(""))
                }
                in 400..499 ->{
                    emit(MyResponse.error(""))
                }
                in 500..599 ->{
                    emit(MyResponse.error(""))
                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun foodList(letter:String):Flow<MyResponse<ResponseFoodsList>>{
        return flow {
            emit(MyResponse.loading())
            when(api.foodsList(letter).code()){
                in 200..202 ->{
                    emit(MyResponse.success(api.foodsList(letter).body()))
                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun foodBySearch(letter:String):Flow<MyResponse<ResponseFoodsList>>{
        return flow {
            emit(MyResponse.loading())
            when(api.searchFood(letter).code()){
                in 200..202 ->{
                    emit(MyResponse.success(api.searchFood(letter).body()))
                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }
    suspend fun foodByCategory(letter:String):Flow<MyResponse<ResponseFoodsList>>{
        return flow {
            emit(MyResponse.loading())
            when(api.foodsByCategory(letter).code()){
                in 200..202 ->{
                    emit(MyResponse.success(api.foodsByCategory(letter).body()))
                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }
}