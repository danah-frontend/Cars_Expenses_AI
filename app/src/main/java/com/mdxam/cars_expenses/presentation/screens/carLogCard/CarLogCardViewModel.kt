package com.mdxam.cars_expenses.presentation.screens.carLogCard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdxam.cars_expenses.data.CarLogRepositoryImpl
import com.mdxam.cars_expenses.domain.CarLog
import com.mdxam.cars_expenses.domain.DeleteCarLogUseCase
import com.mdxam.cars_expenses.domain.GetCarLogByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CarLogCardViewModel.FactoryCarLog::class)
class CarLogCardViewModel @AssistedInject constructor(
    @Assisted
    private  val carLogId: Int,
    private val getCarLogByIdUseCase : GetCarLogByIdUseCase,
    private val deleteCarLogUseCase : DeleteCarLogUseCase
) : ViewModel() {

    @AssistedFactory
    interface FactoryCarLog{
        fun createCarLog(@Assisted carLogId: Int) : CarLogCardViewModel
    }

    private val _state = MutableStateFlow<CarLogCardState>(CarLogCardState.Initial)
    val state = _state.asStateFlow()

    init {
        getCarLogByIdUseCase(carLogId)
            .onEach { log ->
                _state.update { previousState ->
                    when {
                        previousState is CarLogCardState.Finished -> previousState
                        log == null -> CarLogCardState.Finished
                        else -> CarLogCardState.Content(log)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: CarLogCardCommand) {
        when (command) {
            CarLogCardCommand.Back -> {
                _state.update { CarLogCardState.Finished }
            }

            CarLogCardCommand.Delete -> {
                viewModelScope.launch {
                    deleteCarLogUseCase(carLogId = carLogId)
                    _state.update { CarLogCardState.Finished }
                }
            }
        }
    }
}

sealed interface CarLogCardCommand {
    data object Back : CarLogCardCommand
    data object Delete : CarLogCardCommand
}

sealed interface CarLogCardState {
    data object Initial : CarLogCardState
    data class Content(val carLog: CarLog) : CarLogCardState
    data object Finished : CarLogCardState
}