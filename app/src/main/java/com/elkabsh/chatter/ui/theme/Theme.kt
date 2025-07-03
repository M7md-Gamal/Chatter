package com.elkabsh.chatter.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val ChatDarkColorScheme = darkColorScheme(
    primary = ChatBlue,
    onPrimary = TextWhite,
    primaryContainer = ChatBlueDark,
    onPrimaryContainer = TextWhite,

    secondary = MediumGrey,
    onSecondary = TextWhite,
    secondaryContainer = DarkGrey,
    onSecondaryContainer = TextGrey,

    tertiary = AccentYellow,
    onTertiary = DarkBackground,

    background = DarkBackground,
    onBackground = TextWhite,

    surface = DarkGrey,
    onSurface = TextWhite,
    surfaceVariant = MediumGrey,
    onSurfaceVariant = TextGrey,

    outline = TextLight,
    outlineVariant = LightGrey,

    error = AccentRed,
    onError = TextWhite,

    inverseOnSurface = DarkBackground,
    inverseSurface = TextWhite,
    inversePrimary = ChatBlueDark
)

private val ChatLightColorScheme = lightColorScheme(
    primary = ChatBlue,
    onPrimary = TextWhite,
    primaryContainer = ChatBlueLight,
    onPrimaryContainer = DarkBackground,

    secondary = LightGrey,
    onSecondary = TextWhite,
    secondaryContainer = MediumGrey,
    onSecondaryContainer = TextWhite,

    tertiary = AccentOrange,
    onTertiary = TextWhite,

    background = TextWhite,
    onBackground = DarkBackground,

    surface = TextWhite,
    onSurface = DarkBackground,
    surfaceVariant = LightGrey,
    onSurfaceVariant = DarkGrey,

    outline = TextLight,
    outlineVariant = MediumGrey,

    error = AccentRed,
    onError = TextWhite,

    inverseOnSurface = TextWhite,
    inverseSurface = DarkBackground,
    inversePrimary = ChatBlueLight
)

@Composable
fun ChatterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disabled for consistent chat theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> ChatDarkColorScheme
        else -> ChatLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}