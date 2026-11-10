package com.mdxam.cars_expenses.presentation.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object  DateFormatted {

    fun formatDate(timeStamp: Long): String{
        val formatted = SimpleDateFormat("dd.MM.yy",Locale.getDefault())
        return formatted.format(Date(timeStamp))
    }
}