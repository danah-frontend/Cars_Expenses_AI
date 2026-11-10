package com.mdxam.cars_expenses.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CalculateSumCarLogsUseCase @Inject constructor(

    private val repository: CarLogRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.calculateSumCarLogs()
    }
}