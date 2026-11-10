package com.mdxam.cars_expenses.presentation.screens.creation

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdxam.cars_expenses.R
import com.mdxam.cars_expenses.presentation.screens.carLogs.CarLogScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLog(
    modifier: Modifier = Modifier,
    viewModel : CreateLogsViewModel = hiltViewModel(),
    onFinished : () -> Unit
){
    val state = viewModel.state.collectAsState()

    val currentState = state.value

    when(currentState){
        is CreateLogState.Creation -> {
            Scaffold(
                modifier = modifier,
                topBar = {
                    CenterAlignedTopAppBar(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        title = {
                            Text(
                                text = "Добавление записи",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.clickable{
                                    viewModel.processCommand(CreateLogCommand.Back)
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
                        value = currentState.title,
                        placeholder = "Введите название записи...",
                        onValueChange = {
                            viewModel.processCommand(CreateLogCommand.InputTitle(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    InputForm(
                        title = "Сумма",
                        value = if (currentState.amount == 0L) "" else currentState.amount.toString(),
                        placeholder = "Введите сумму...",
                        keyBoardOption = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        onValueChange = {
                            viewModel.processCommand(CreateLogCommand.InputAmount(it.toLongOrNull() ?: 0))
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    InputForm(
                        modifier = Modifier.weight(1f).height(180.dp),
                        title = "Описание",
                        value = currentState.description,
                        placeholder = "Добавьте описание...",
                        onValueChange = {
                            viewModel.processCommand(CreateLogCommand.InputDescription(it))
                        },
                        singleLines = false,
                        minLines = 6
                    )

                    ButtonAdd(
                        modifier = Modifier.padding(horizontal = 18.dp),
                        onClick = {
                            viewModel.processCommand(CreateLogCommand.Save)
                        },
                        isEnabled = currentState.isSaveEnabled
                    )
                }
            }
        }
        CreateLogState.Finished -> {
            LaunchedEffect(key1 = Unit) {
                onFinished()
            }
        }
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