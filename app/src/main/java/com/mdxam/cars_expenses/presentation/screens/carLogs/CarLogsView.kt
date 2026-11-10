package com.mdxam.cars_expenses.presentation.screens.carLogs

import android.content.Context
import android.icu.text.CaseMap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.query
import com.mdxam.cars_expenses.R
import com.mdxam.cars_expenses.domain.CarLog
import com.mdxam.cars_expenses.presentation.utils.DateFormatted

@Composable
fun CarLogs(
    modifier: Modifier = Modifier,
    viewModel: CarLogsViewModel = hiltViewModel(),
    onAddClick: () -> Unit,
    onLogClick : (CarLog) -> Unit
    ){
    val state = viewModel.state.collectAsState()

    val currentState = state.value

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(15.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add_log"
                    )
                }
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(paddingValues = innerPadding)
        ) {
            SearchBar(
                modifier = Modifier.padding(top = 8.dp, start = 20.dp, end = 20.dp),
                query = currentState.query,
                onQueryChange = {viewModel.processCommand(CarLogCommand.InputSearchQuery(it))}
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Card(
                modifier = Modifier,
                amount = currentState.amount
            )
            Spacer(modifier = Modifier.height(24.dp))

            Title(
                text = "Добавленные записи",
                modifier = Modifier.padding(horizontal = 18.dp)
            )
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                items(currentState.logs){carLog ->
                    ListElement(
                        carLog = carLog,
                        onDeleteClick = {
                            viewModel.processCommand(CarLogCommand.DeleteCarLog(carLog.id))
                        },
                        onClick = { clickedLog ->
                            onLogClick(clickedLog)
                        })
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange : (String) -> Unit
){
    TextField(
        modifier = modifier
            .fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = "Search",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search",
                tint = MaterialTheme.colorScheme.outlineVariant
            )
        },
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}

@Composable
private fun Card(
    modifier: Modifier = Modifier,
    amount : Int
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .rotate(-6f)
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(16.dp)
                )
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .rotate(6f)
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(16.dp)
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer
                )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Kia Rio 3 2012",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                Text(
                    text = "Р ${amount.toString()}.00",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 34.sp
                )
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
                Row(
                    modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Т425АР287",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Icon(
                        painter = painterResource(R.drawable.kia_logo_icon_181338),
                        contentDescription = "kia motors",
                        modifier = Modifier.height(44.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    text : String
){
    Text(modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.secondary,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ListElement(
    modifier: Modifier = Modifier,
    carLog: CarLog,
    onDeleteClick : () -> Unit,
    onClick : (CarLog) -> Unit
){
    Row(
        modifier = modifier.fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(16.dp,8.dp)
            .clickable {
                onClick(carLog)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(34.dp)
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(15.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = DateFormatted.formatDate(carLog.timestamp),
                modifier = Modifier.wrapContentSize(unbounded = true).rotate(-90f),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(15.dp))
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        ){
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(color = MaterialTheme.colorScheme.outlineVariant)
                    .padding(end = 16.dp, top = 4.dp, start = 12.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        text = carLog.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = carLog.amount.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        text = carLog.description,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = onDeleteClick
                    ) {
                        Icon(
//                            modifier = Modifier.size(32.dp),
                            painter = painterResource(R.drawable.vector),
                            contentDescription = "delete_car_log",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}