package com.strabled.composepreferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * A composable function that represents a basic [PreferenceItem].
 *
 * @param modifier The modifier to be applied to the [PreferenceItem].
 * @param enabled Whether the [PreferenceItem] is enabled.
 * @param darkenOnDisable Whether to darken the text when the [PreferenceItem] is disabled.
 * @param leadingIcon A composable function to display a leading icon.
 * @param secondaryText A composable function to display secondary text.
 * @param makeMinimal Whether to make the [PreferenceItem] minimal in height.
 * @param trailingContent A composable function to display trailing content.
 * @param text A composable function to display the main text.
 */
@Composable
fun BasicPreference(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    secondaryText: @Composable (() -> Unit)? = null,
    makeMinimal: Boolean = false,
    trailingContent: @Composable () -> Unit = {},
    text: @Composable () -> Unit
) {
    Row(
        modifier = modifier then
                Modifier
                    .heightIn(min = if (makeMinimal) MinDimensions.minimalHeight else MinDimensions.minHeight)
                    .padding(
                        start = PreferenceTheme.spacing.preferenceHorizontalPadding,
                        end = PreferenceTheme.spacing.preferenceHorizontalPadding,
                        top = when {
                            secondaryText == null && !makeMinimal -> PreferenceTheme.spacing.preferenceSingleLineVerticalPadding
                            else -> PreferenceTheme.spacing.preferenceVerticalPadding
                        },
                        bottom = when {
                            makeMinimal -> 0.dp
                            secondaryText == null -> PreferenceTheme.spacing.preferenceSingleLineVerticalPadding
                            else -> PreferenceTheme.spacing.preferenceVerticalPadding
                        }
                    )
                    .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Box(
                modifier = Modifier.sizeIn(minWidth = MinDimensions.minIconSize, minHeight = MinDimensions.minIconSize),
                contentAlignment = Alignment.CenterStart
            ) {
                CompositionLocalProvider(LocalContentColor provides PreferenceTheme.colorScheme.leadingIconColor) {
                    leadingIcon()
                }
            }
        }

        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
            ProvideTextStyle(
                PreferenceTheme.typography.titleStyle.applyColor(
                    PreferenceTheme.colorScheme.titleColor,
                    if (enabled || !darkenOnDisable) AlphaHigh else AlphaDisabled
                )
            ) {
                text()
            }
            CompositionLocalProvider(
                LocalTextStyle provides PreferenceTheme.typography.summaryStyle.applyColor(
                    PreferenceTheme.colorScheme.summaryColor,
                    if (enabled || !darkenOnDisable) AlphaMedium else AlphaDisabled
                ),
                content = { secondaryText?.invoke() }
            )
        }
        ProvideTextStyle(
            PreferenceTheme.typography.trailingContentStyle.applyColor(
                PreferenceTheme.colorScheme.trailingContentColor,
                if (enabled || !darkenOnDisable) AlphaHigh else AlphaDisabled
            )
        ) {
            trailingContent()
        }
    }
}

private fun TextStyle.applyColor(color: Color, alpha: Float = 1f): TextStyle = copy(color = color.copy(alpha = alpha))

internal fun applyClickable(enabled: Boolean, onClick: () -> Unit): Modifier = if (enabled) Modifier.clickable(onClick = onClick) else Modifier

internal object MinDimensions {
    val minHeight = 48.dp
    val minimalHeight = 32.dp
    val minIconSize = 40.dp
}

private const val AlphaHigh = 1.0f
private const val AlphaMedium = 0.75f
private const val AlphaDisabled = 0.38f