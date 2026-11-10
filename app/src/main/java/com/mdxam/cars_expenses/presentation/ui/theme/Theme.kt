package com.mdxam.cars_expenses.presentation.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(

)

private val LightColorScheme = lightColorScheme(
// Главные акценты (кнопки и заголовки с суммами)
    primary = Gray900,
    onPrimary = White,

    // Базовый фон экранов и максимально контрастный текст на нем
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,

    // Фон самих записей и их описание/иконки
    surfaceVariant = Gray700,
    onSurfaceVariant = Gray500,

    // Обычные тексты, не являющиеся главными заголовками
    secondary = Gray600,

    // Плейсхолдеры в формах
    outlineVariant = Gray400,

    // Контейнеры для поиска и фона информации
    surfaceContainer = Gray100,

    // Самые темные фоновые элементы (декор под карточкой авто)
    surfaceContainerHighest = Gray300
)

@Composable
fun Cars_ExpensesTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}