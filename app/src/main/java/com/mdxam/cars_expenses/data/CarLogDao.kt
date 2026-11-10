package com.mdxam.cars_expenses.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CarLogDao {

    @Upsert
    suspend fun addCarLog(carLog: CarLogDb)

    @Query("DELETE FROM carLog WHERE id == :carLogId")
    suspend fun deleteCarLog(carLogId : Int)

    @Query("SELECT * FROM carLog ORDER BY timestamp DESC")
    fun getAllCarLogs(): Flow<List<CarLogDb>>

    @Query("SELECT * FROM carLog WHERE id = :carLogId")
    fun getCarLog(carLogId: Int): Flow<CarLogDb?>

    @Query("SELECT * FROM carLog WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchCarLogs(query: String) : Flow<List<CarLogDb>>

    @Query("SELECT IFNULL(SUM(amount),0) FROM carLog")
    fun calculateSumCarLogs() : Flow<Int>
}