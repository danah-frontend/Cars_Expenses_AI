package com.mdxam.cars_expenses.presentation.screens.edited

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdxam.cars_expenses.data.CarLogRepositoryImpl
import com.mdxam.cars_expenses.domain.CarLog
import com.mdxam.cars_expenses.domain.DeleteCarLogUseCase
import com.mdxam.cars_expenses.domain.EditCarLogUseCase
import com.mdxam.cars_expenses.domain.GetCarLogByIdUseCase
import com.mdxam.cars_expenses.presentation.screens.carLogCard.CarLogCardViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditLogsViewModel.FactoryEditLogs::class)
class EditLogsViewModel @AssistedInject constructor(
    @Assisted
    private  val carLogId: Int,
    private val editCarLogUseCase : EditCarLogUseCase,
    private val getCarLogByIdUseCase : GetCarLogByIdUseCase
) : ViewModel() {

    @AssistedFactory
    interface FactoryEditLogs{
        fun createEdit(@Assisted carLogId: Int) : EditLogsViewModel
    }
    private val _state = MutableStateFlow<EditLogState>(EditLogState.Initial)

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val carLog = getCarLogByIdUseCase(carLogId).first()

            _state.value = if (carLog != null) {
                EditLogState.Editing(carLog)
            } else {
                EditLogState.Finished
            }
        }
    }

    fun processCommand(command: EditLogCommand){
        when(command){
            EditLogCommand.Back -> {
                _state.update { EditLogState.Finished }
            }
            is EditLogCommand.InputAmount -> {
                _state.update { previousState ->
                    if(previousState is EditLogState.Editing){
                        val newCarLog = previousState.carLog.copy(amount = command.amount)
                        previousState.copy(carLog = newCarLog)
                    }else{
                       previousState
                    }
                }
            }
            is EditLogCommand.InputDescription -> {
                _state.update { previousState ->
                    if(previousState is EditLogState.Editing){
                        val newCarLog = previousState.carLog.copy(description = command.description)
                        previousState.copy(carLog = newCarLog)
                    }else{
                        previousState
                    }
                }
            }
            is EditLogCommand.InputTitle -> {
                _state.update { previousState ->
                    if(previousState is EditLogState.Editing){
                        val newCarLog = previousState.carLog.copy(title = command.title)
                        previousState.copy(carLog = newCarLog)
                    }else{
                        previousState
                    }
                }
            }

            EditLogCommand.Save -> {
                viewModelScope.launch{
                    _state.update {previousState ->
                        if (previousState is EditLogState.Editing){
                            val carLog = previousState.carLog
                            editCarLogUseCase(carLog = carLog)
                            EditLogState.Finished
                        }else{
                            previousState
                        }
                    }
                }

            }
        }
    }
}

sealed interface EditLogCommand{

    data class InputTitle(
        val title : String
    ): EditLogCommand

    data class InputAmount(
        val amount : Long
    ): EditLogCommand

    data class InputDescription(
        val description : String
    ): EditLogCommand

    data object Save: EditLogCommand

    data object Back: EditLogCommand
}

sealed interface EditLogState{

    data object Initial: EditLogState
    data class Editing(
        val carLog: CarLog
    ): EditLogState{
        val isSaveEnabled: Boolean
            get() = carLog.title.isNotBlank() && carLog.description.isNotBlank() && carLog.amount.toString().isNotBlank()
    }

    data object Finished: EditLogState
}
