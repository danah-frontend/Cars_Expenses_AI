package com.mdxam.cars_expenses.domain

import javax.inject.Inject

class AddCarLogUseCase @Inject constructor(

    private val repository: CarLogRepository
) {

    suspend operator fun invoke(
        title: String,
        amount: Long,
        description: String
    ){
        repository.addCarLog(title = title,
            amount=amount,description=description, timestamp = System.currentTimeMillis())
    }
}