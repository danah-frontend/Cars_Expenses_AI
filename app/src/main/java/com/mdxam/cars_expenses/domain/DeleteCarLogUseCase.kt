package com.mdxam.cars_expenses.domain

import javax.inject.Inject

class DeleteCarLogUseCase @Inject constructor(

    private val repository: CarLogRepository
) {
    suspend operator fun invoke(carLogId:Int){
        repository.deleteCarLog(carLogId)
    }
}