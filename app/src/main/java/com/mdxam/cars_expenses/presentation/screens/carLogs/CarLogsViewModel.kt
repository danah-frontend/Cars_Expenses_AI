package com.mdxam.cars_expenses.presentation.screens.carLogs

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.mdxam.cars_expenses.data.CarLogRepositoryImpl
import com.mdxam.cars_expenses.domain.CalculateSumCarLogsUseCase
import com.mdxam.cars_expenses.domain.CarLog
import com.mdxam.cars_expenses.domain.DeleteCarLogUseCase
import com.mdxam.cars_expenses.domain.GetAllCarLogsUseCase
import com.mdxam.cars_expenses.domain.SearchCarLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarLogsViewModel @Inject constructor(
    private val getAllCarLogsUseCase : GetAllCarLogsUseCase,

    private val deleteCarLogUseCase : DeleteCarLogUseCase,

    private val searchCarLogsUseCase : SearchCarLogsUseCase,

    private val calculateSumCarLogsUseCase : CalculateSumCarLogsUseCase
): ViewModel() {

    private val query = MutableStateFlow("")
    private val _state = MutableStateFlow(CarLogScreenState())

    val state = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        query
            .onEach {input ->
                _state.update { it.copy(query = input) }
            }
            .flatMapLatest { input ->
                if(input.isBlank()){
                    getAllCarLogsUseCase()
                }else{
                    searchCarLogsUseCase(input)
                }
            }
            .onEach {logs ->
                _state.update { it.copy(logs = logs) }
            }
            .launchIn(scope)

        calculateSumCarLogsUseCase()
            .onEach {total ->
                _state.update { it.copy(amount = total) }
            }
            .launchIn(scope)
    }

    fun processCommand(command: CarLogCommand){
        when(command){
            is CarLogCommand.DeleteCarLog -> {
                viewModelScope.launch {
                    deleteCarLogUseCase(command.id)
                }
            }
            is CarLogCommand.InputSearchQuery -> {
                query.update { command.query.trim() }
            }
        }
    }
}

sealed interface CarLogCommand{

    data class InputSearchQuery(val query: String): CarLogCommand

    data class DeleteCarLog(val id: Int) : CarLogCommand
}

data class CarLogScreenState(
    val query: String = "",
    val amount: Int = 0,
    val logs: List<CarLog> = listOf()
)