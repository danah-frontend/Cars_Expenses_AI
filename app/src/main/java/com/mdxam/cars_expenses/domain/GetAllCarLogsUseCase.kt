package com.mdxam.cars_expenses.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCarLogsUseCase @Inject constructor(
    private val repository: CarLogRepository
) {

    operator fun invoke(): Flow<List<CarLog>> {
        return repository.getAllCarLogs()
    }
}