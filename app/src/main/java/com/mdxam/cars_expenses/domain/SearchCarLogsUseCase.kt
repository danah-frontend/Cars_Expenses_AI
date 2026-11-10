package com.mdxam.cars_expenses.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCarLogsUseCase @Inject constructor(
    private val repository: CarLogRepository
) {

    operator fun invoke(query : String): Flow<List<CarLog>> {
        return repository.searchCarLogs(query)
    }
}