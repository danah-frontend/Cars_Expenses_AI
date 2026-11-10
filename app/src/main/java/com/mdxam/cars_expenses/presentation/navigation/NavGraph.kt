package com.mdxam.cars_expenses.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mdxam.cars_expenses.presentation.screens.carLogCard.CarLogCard
import com.mdxam.cars_expenses.presentation.screens.carLogs.CarLogs
import com.mdxam.cars_expenses.presentation.screens.creation.CreateLog
import com.mdxam.cars_expenses.presentation.screens.edited.EditLog
import kotlinx.serialization.Serializable

@Composable
fun NavGraph(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.CarLogs
    ){
        composable<Screen.CarLogs> {
            CarLogs(
                onLogClick = {
                    navController.navigate(Screen.CarLogCard(it.id))},
                onAddClick = {navController.navigate(Screen.AddCarLog)}
            )
        }
        composable<Screen.AddCarLog> {
            CreateLog(
                onFinished = {navController.popBackStack()}
            )
        }
        composable<Screen.CarLogCard> { backStackEntry ->
            val carLogRoute: Screen.CarLogCard = backStackEntry.toRoute()
            CarLogCard(
                carLogId = carLogRoute.carLogId,
                onFinished = { navController.popBackStack() },
                onEditClick = { navController.navigate(Screen.EditCarLog(carLogRoute.carLogId)) }
            )
        }
        composable<Screen.EditCarLog> { backStackEntry ->
            val editRoute: Screen.EditCarLog = backStackEntry.toRoute()
            EditLog(
                carLogId = editRoute.carLogId,
                onFinished = { navController.popBackStack() }
            )
        }
    }

}
@Serializable
sealed interface Screen{
    @Serializable
    data object CarLogs: Screen
    @Serializable
    data object AddCarLog: Screen
    @Serializable
    data class EditCarLog(
        val carLogId: Int
    ) : Screen
    @Serializable
    data class CarLogCard(
        val carLogId: Int
    ) : Screen
}