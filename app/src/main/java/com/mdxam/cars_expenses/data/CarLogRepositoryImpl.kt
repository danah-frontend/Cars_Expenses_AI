package com.mdxam.cars_expenses.data

import android.content.Context
import com.mdxam.cars_expenses.domain.CarLog
import com.mdxam.cars_expenses.domain.CarLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarLogRepositoryImpl @Inject constructor(
    private val carLogDao : CarLogDao
): CarLogRepository {

    override suspend fun addCarLog(
        title: String,
        amount: Long,
        description: String,
        timestamp: Long
    ) {
        val carLogDb = CarLogDb(0,title,amount,description,timestamp)
        carLogDao.addCarLog(carLogDb)
    }

    override suspend fun deleteCarLog(carLogId: Int) {
        carLogDao.deleteCarLog(carLogId)
    }

    override suspend fun editCarLog(carLog: CarLog) {
        carLogDao.addCarLog(carLog.entityToDb())
    }

    override fun getAllCarLogs(): Flow<List<CarLog>> {
        return carLogDao.getAllCarLogs().map {carLogList ->
            carLogList.map { carLog ->
                carLog.dbToEntity()
            }
        }
    }

    override fun getCarLogById(carLogId: Int): Flow<CarLog?> {
        return carLogDao.getCarLog(carLogId).map { it?.dbToEntity() }
    }

    override fun searchCarLogs(query: String): Flow<List<CarLog>> {
        return carLogDao.searchCarLogs(query).map {carLogList ->
            carLogList.map { carLog ->
                carLog.dbToEntity()
            }
        }
    }

    override fun calculateSumCarLogs(): Flow<Int> {
        return carLogDao.calculateSumCarLogs()
    }
}