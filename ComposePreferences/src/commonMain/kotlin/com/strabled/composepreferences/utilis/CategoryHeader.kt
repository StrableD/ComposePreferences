package com.strabled.composepreferences.utilis

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.strabled.composepreferences.PreferenceCategory
import com.strabled.composepreferences.PreferenceFooter
import com.strabled.composepreferences.PreferenceTheme

/**
 * A composable function that displays a [PreferenceCategory] header with a given title.
 *
 * @param title The title to be displayed in the category header.
 */
@Composable
fun CategoryHeader(
    title: String,
) {
    Box(
        modifier = Modifier
            .padding(start = PreferenceTheme.spacing.categotyTitleIndent + PreferenceTheme.spacing.preferenceHorizontalPadding)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            color = PreferenceTheme.colorScheme.categoryHeaderColor,
            fontSize = LocalTextStyle.current.fontSize.times(FontSizeMultiplier),
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * A [Composable] function that displays a [PreferenceFooter] with an optional leading [icon], [title], and [subtitle].
 * The [PreferenceFooter] is [clickable] when [enabled] and triggers the provided [onClick] action.
 *
 * @param icon A [Composable] function to display a leading [Icon]. Defaults to an empty [Composable].
 * @param title The title to be displayed in the [PreferenceFooter].
 * @param onClick A lambda function to be invoked when the [PreferenceFooter] is clicked. Defaults to an empty lambda.
 * @param enabled A boolean value indicating whether the [PreferenceFooter] is enabled or disabled. Defaults to false.
 * @param subtitle An optional [Composable] function to display a subtitle. Defaults to null meaning no subtitle is displayed.
 */
@Composable
fun Footer(
    icon: @Composable () -> Unit = {},
    title: String,
    onClick: () -> Unit = {},
    enabled: Boolean = false,
    subtitle: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .padding(horizontal = PreferenceTheme.spacing.preferenceHorizontalPadding, vertical = PreferenceTheme.spacing.preferenceVerticalPadding)
            .fillMaxWidth()
            then if(enabled) Modifier.clickable(onClick = onClick) else Modifier,
        horizontalArrangement = Arrangement.spacedBy(PreferenceTheme.spacing.preferenceScreenContentGap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalContentColor provides Color.Gray
        ) {
            icon()
            Column {
                Text(text = title, style = PreferenceTheme.typography.titleStyle)
                ProvideTextStyle(PreferenceTheme.typography.summaryStyle) {
                    subtitle?.invoke()
                }
            }
        }
    }
}

private const val FontSizeMultiplier = 0.85f