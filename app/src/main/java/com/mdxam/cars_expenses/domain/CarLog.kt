package com.mdxam.cars_expenses.domain

data class CarLog(
    val id: Int,
    val title: String,
    val amount: Long,
    val description: String,
    val timestamp: Long
)
