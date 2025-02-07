package com.strabled.composepreferences

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A container for [PreferenceItem]s for more flexible stylization.
 * The container is styled using [MaterialTheme]. You can override the styling by providing custom [PreferenceColorTheme], [PreferenceTypography] and [PreferenceSpacing].
 * The container uses the [Surface] composable to provide the basic structure.
 *
 * @param modifier The [Modifier] to be applied to the container.
 * @param theme The [color theme][PreferenceColorTheme] for the preferences and the container. The default is [lightPreferenceColorTheme].
 * @param typography The typography to be used for the preferences and the container.
 * @param spacing The spacing to be used for the preferences and the screen.
 * @param content The content of the preference container, defined within a PreferenceScope.
 * @see Surface
 * @see PreferenceColorTheme
 * @see PreferenceTypography
 * @see PreferenceSpacing
 * @see PreferenceTheme
 * @see PreferenceItem
 */
@Composable
fun PreferenceContainer(
    modifier: Modifier,
    theme: PreferenceColorTheme = PreferenceTheme.colorScheme,
    typography: PreferenceTypography = PreferenceTheme.typography,
    spacing: PreferenceSpacing = PreferenceTheme.spacing,
    content: PreferenceScope.() -> Unit
) {
    val preferenceScope = PreferenceScopeImpl().apply(content)

    ProvidePreferenceTheme(theme, typography, spacing) {
        Surface {
            LazyColumn(modifier = modifier) {
                items(preferenceScope.preferenceItemNumber) { index ->
                    preferenceScope.getPreferenceItem(index)()

                    if (PreferenceTheme.spacing.dividerThickness != 0.dp && preferenceScope.needsDivider(index)) {
                        HorizontalDivider(
                            color = PreferenceTheme.colorScheme.dividerColor,
                            thickness = PreferenceTheme.spacing.dividerThickness,
                            modifier = Modifier.padding(horizontal = PreferenceTheme.spacing.dividerIndent)
                        )
                    }
                }
                item {
                    preferenceScope.getFooter()()
                }
            }
        }
    }
}