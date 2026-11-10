package com.mdxam.cars_expenses.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCarLogByIdUseCase @Inject constructor(
    private val repository: CarLogRepository
) {
    operator fun invoke(carLogId: Int): Flow<CarLog?> {
        return repository.getCarLogById(carLogId)
    }
}