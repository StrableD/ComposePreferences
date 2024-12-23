package com.strabled.composepreferences.preferences

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.strabled.composepreferences.*

/**
 * A [Composable] function that represents a text [preference item][PreferenceItem].
 *
 * This function can be used to create a customizable text [preference item][PreferenceItem] in a [settings screen][PreferenceScreen].
 *
 * @param title The title text to be displayed.
 * @param modifier The [Modifier] to be applied to the [preference item][PreferenceItem].
 * @param summary An optional [Composable] function to display a summary below the [title].
 * @param enabled A boolean indicating whether the [preference item][PreferenceItem] is enabled.
 * @param darkenOnDisable A boolean indicating whether to darken the [preference item][PreferenceItem] when disabled.
 * @param makeMinimal A boolean indicating whether to make the [preference item][PreferenceItem] minimal.
 * @param onClick A lambda function to be invoked when the [preference item][PreferenceItem] is clicked.
 * @param leadingIcon An optional [Composable] function to display a leading [icon][androidx.compose.material3.Icon].
 * @param trailingContent A [Composable] function to display trailing content.
 *
 * Example usage:
 * ```
 * TextPreference(
 *     title = "Example Preference",
 *     summary = { Text("This is a summary") },
 *     enabled = true,
 *     onClick = { /* Handle click */ },
 *     leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
 *     trailingContent = { Switch(checked = true, onCheckedChange = {}) }
 * )
 * ```
 */
@Composable
fun TextPreference(
    title: String,
    modifier: Modifier = Modifier,
    summary: @Composable (() -> Unit)? = null,
    enabled: Boolean = false,
    darkenOnDisable: Boolean = false,
    makeMinimal: Boolean = false,
    onClick: () -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingContent: @Composable () -> Unit = {}
) {
    BasicPreference(
        modifier = modifier then applyClickable(onClick = onClick, enabled = enabled),
        enabled = enabled,
        darkenOnDisable = darkenOnDisable,
        leadingIcon = leadingIcon,
        trailingContent = trailingContent,
        text = {
            Text(
                text = title,
                Modifier
                    .fillMaxWidth()
                    .padding(start = PreferenceTheme.spacing.preferenceTitleIndent),
                color = PreferenceTheme.colorScheme.titleColor
            )
        },
        makeMinimal = makeMinimal,
        secondaryText = summary,
    )
}