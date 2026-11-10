package com.mdxam.cars_expenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mdxam.cars_expenses.presentation.navigation.NavGraph
import com.mdxam.cars_expenses.presentation.screens.carLogs.CarLogs
import com.mdxam.cars_expenses.presentation.screens.creation.CreateLog
import com.mdxam.cars_expenses.presentation.screens.edited.EditLog
import com.mdxam.cars_expenses.presentation.ui.theme.Cars_ExpensesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cars_ExpensesTheme {
                NavGraph()
            }
        }
    }
}
