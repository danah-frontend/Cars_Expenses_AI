package com.mdxam.cars_expenses.presentation.screens.creation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdxam.cars_expenses.data.CarLogRepositoryImpl
import com.mdxam.cars_expenses.domain.AddCarLogUseCase
import com.mdxam.cars_expenses.presentation.screens.carLogCard.CarLogCardViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateLogsViewModel @Inject constructor(
    private val addCarLogUseCase : AddCarLogUseCase
): ViewModel(){
    private val _state = MutableStateFlow<CreateLogState>(CreateLogState.Creation())

    val state = _state.asStateFlow()

    fun processCommand(command: CreateLogCommand){
        when(command){
            CreateLogCommand.Back -> {
                _state.update { CreateLogState.Finished }
            }
            is CreateLogCommand.InputAmount -> {
                _state.update { previousState ->
                    if(previousState is CreateLogState.Creation){
                        previousState.copy(
                            amount = command.amount,
                            isSaveEnabled = previousState.title.isNotBlank() && previousState.description.isNotBlank() && command.amount.toString().isNotBlank()
                        )
                    }else{
                        CreateLogState.Creation(amount = command.amount)
                    }
                }
            }
            is CreateLogCommand.InputDescription -> {
                _state.update { previousState ->
                    if(previousState is CreateLogState.Creation){
                        previousState.copy(
                            description = command.description,
                            isSaveEnabled = previousState.amount.toString().isNotBlank() && previousState.title.isNotBlank() && command.description.isNotBlank()
                        )
                    }else{
                        CreateLogState.Creation(description = command.description)
                    }
                }
            }
            is CreateLogCommand.InputTitle -> {
                _state.update { previousState ->
                    if(previousState is CreateLogState.Creation){
                        previousState.copy(
                            title = command.title,
                            isSaveEnabled = previousState.amount.toString().isNotBlank() && previousState.description.isNotBlank() && command.title.isNotBlank()
                        )
                    }else{
                        CreateLogState.Creation(description = command.title)
                    }
                }
            }

            CreateLogCommand.Save -> {
                viewModelScope.launch{
                    _state.update {previousState ->
                        if (previousState is CreateLogState.Creation){
                            val title = previousState.title
                            val amount = previousState.amount
                            val description = previousState.description
                            addCarLogUseCase(title, amount, description)
                            CreateLogState.Finished
                        }else{
                            previousState
                        }
                    }
                }

            }
        }
    }
}

sealed interface CreateLogCommand{

    data class InputTitle(
        val title : String
    ): CreateLogCommand

    data class InputAmount(
        val amount : Long
    ): CreateLogCommand

    data class InputDescription(
        val description : String
    ): CreateLogCommand

    data object Save: CreateLogCommand

    data object Back: CreateLogCommand
}

sealed interface CreateLogState{
    data class Creation(
        val title : String = "",
        val amount : Long = 0L,
        val description : String = "",
        val isSaveEnabled : Boolean = false
    ): CreateLogState

    data object Finished: CreateLogState
}
