package com.mdxam.cars_expenses.domain

import kotlinx.coroutines.flow.Flow

interface CarLogRepository {

    suspend fun addCarLog(title: String,
                      amount: Long,
                      description: String,
                      timestamp: Long)

    suspend fun deleteCarLog(carLogId: Int)

    suspend fun editCarLog(carLog: CarLog)

    fun getAllCarLogs(): Flow<List<CarLog>>

    fun getCarLogById(carLogId: Int): Flow<CarLog?>

    fun searchCarLogs(query: String): Flow<List<CarLog>>

    fun calculateSumCarLogs(): Flow<Int>
}