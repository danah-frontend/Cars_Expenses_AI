package com.mdxam.cars_expenses.presentation.screens.edited

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdxam.cars_expenses.R
import com.mdxam.cars_expenses.presentation.screens.carLogCard.CarLogCardViewModel
import com.mdxam.cars_expenses.presentation.screens.edited.EditLogCommand.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLog(
    modifier: Modifier = Modifier,
    carLogId : Int,
    viewModel : EditLogsViewModel = hiltViewModel(
        creationCallback = {factory : EditLogsViewModel.FactoryEditLogs ->
            factory.createEdit(carLogId)
        }
    ),
    onFinished : () -> Unit
){
    val state = viewModel.state.collectAsState()

    val currentState = state.value

    when(currentState){
        is EditLogState.Editing -> {
            Scaffold(
                modifier = modifier,
                topBar = {
                    CenterAlignedTopAppBar(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        title = {
                            Text(
                                text = "Изменение записи",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.clickable{
                                    viewModel.processCommand(EditLogCommand.Back)
                                }.size(32.dp),
                                painter = painterResource(R.drawable.chevron_left),
                                contentDescription = "back_to_list",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }
            ) {innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    InputForm(
                        title = "Название",
                        value = currentState.carLog.title,
                        placeholder = "Введите название записи...",
                        onValueChange = {
                            viewModel.processCommand(InputTitle(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    InputForm(
                        title = "Сумма",
                        value = if (currentState.carLog.amount == 0L) "" else currentState.carLog.amount.toString(),
                        placeholder = "Введите сумму...",
                        keyBoardOption = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        onValueChange = {
                            viewModel.processCommand(InputAmount(it.toLongOrNull() ?: 0))
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    InputForm(
                        modifier = Modifier.weight(1f).height(180.dp),
                        title = "Описание",
                        value = currentState.carLog.description,
                        placeholder = "Добавьте описание...",
                        onValueChange = {
                            viewModel.processCommand(InputDescription(it))
                        },
                        singleLines = false,
                        minLines = 6
                    )

                    ButtonAdd(
                        modifier = Modifier.padding(horizontal = 18.dp),
                        onClick = {
                            viewModel.processCommand(Save)
                        },
                        isEnabled = currentState.isSaveEnabled
                    )
                }
            }
        }
        EditLogState.Finished -> {
            LaunchedEffect(key1 = Unit) {
                onFinished()
            }
        }

        EditLogState.Initial -> {}
    }
}

@Composable
private fun InputForm(
    modifier: Modifier = Modifier,
    title : String,
    value : String,
    placeholder : String,
    keyBoardOption: KeyboardOptions = KeyboardOptions.Default,
    onValueChange : (String) -> Unit,
    singleLines : Boolean = true,
    minLines : Int = 1
){
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            singleLine = singleLines,
            onValueChange = onValueChange,
            minLines = minLines,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = keyBoardOption,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun ButtonAdd(
    modifier: Modifier = Modifier,
    isEnabled : Boolean = false,
    onClick : () -> Unit
){
    Button(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
        ),
        enabled = isEnabled,
        shape = RoundedCornerShape(18.dp),
        onClick = onClick,
        content = {
            Text(
                text = "Сохранить запись",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.surface
            )
        }
    )
}