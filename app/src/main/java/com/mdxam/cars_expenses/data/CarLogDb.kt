package com.mdxam.cars_expenses.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carLog")
data class CarLogDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val amount: Long,
    val description: String,
    val timestamp: Long
)
