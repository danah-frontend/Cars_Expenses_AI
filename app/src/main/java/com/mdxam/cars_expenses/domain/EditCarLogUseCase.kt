package com.mdxam.cars_expenses.domain

import javax.inject.Inject

class EditCarLogUseCase @Inject constructor(

    private val repository: CarLogRepository
) {
    suspend operator fun invoke(carLog: CarLog){
        repository.editCarLog(carLog)
    }

}