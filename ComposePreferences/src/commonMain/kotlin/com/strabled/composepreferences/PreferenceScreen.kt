package com.strabled.composepreferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Main preference screen holding all the [PreferenceItem]s.
 * The screen is styled using [MaterialTheme]. You can override the styling by providing custom [PreferenceColorTheme], [PreferenceTypography] and [PreferenceSpacing].
 * The screen uses the [Scaffold] composable to provide the basic structure. You can provide the top bar, bottom bar, snackbar host, floating action button and floating action button position using the [ScaffholdComponents].
 *
 * @param modifier The [Modifier] to be applied to the screen.
 * @param scaffoldComponents All the [ScaffholdComponents] for the preference screen.
 * @param theme The [color theme][PreferenceColorTheme] for the preferences and the screen. The default is [lightPreferenceColorTheme].
 * @param typography The typography to be used for the preferences and the screen.
 * @param spacing The spacing to be used for the preferences and the screen.
 * @param content The content of the preference screen, defined within a PreferenceScope.
 * @see ScaffholdComponents
 * @see Scaffold
 * @see PreferenceColorTheme
 * @see PreferenceTypography
 * @see PreferenceSpacing
 * @see PreferenceTheme
 * @see PreferenceItem
 */
@Composable
fun PreferenceScreen(
    modifier: Modifier = Modifier,
    scaffoldComponents: ScaffholdComponents = ScaffholdComponents(),
    theme: PreferenceColorTheme = PreferenceTheme.colorScheme,
    typography: PreferenceTypography = PreferenceTheme.typography,
    spacing: PreferenceSpacing = PreferenceTheme.spacing,
    content: PreferenceScope.() -> Unit
) {
    val preferenceScope = PreferenceScopeImpl().apply(content)

    CompositionLocalProvider(
        LocalPreferenceColorTheme provides theme,
        LocalPreferenceTypography provides typography,
        LocalPreferenceSpacing provides spacing
    ) {
        Scaffold(
            topBar = scaffoldComponents.topBar,
            bottomBar = scaffoldComponents.bottomBar,
            snackbarHost = scaffoldComponents.snackbarHost,
            floatingActionButton = scaffoldComponents.floatingActionButton,
            floatingActionButtonPosition = scaffoldComponents.floatingActionButtonPosition
        ) { padding ->
            Column {
                Spacer(modifier = Modifier.height(PreferenceTheme.spacing.preferenceScreenDownset))
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize() then modifier
                ) {
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
}

/**
 * Data class representing the components of the Scaffold.
 * It is used in the [PreferenceScreen] to provide the [topBar], [bottomBar], [snackbarHost], [floatingActionButton] and [floatingActionButtonPosition] to the screen.
 *
 * @property topBar A composable function for the top bar.
 * @property bottomBar A composable function for the bottom bar.
 * @property snackbarHost A composable function for the snackbar host.
 * @property floatingActionButton A composable function for the floating action button.
 * @property floatingActionButtonPosition The position of the floating action button.
 * @see PreferenceScreen
 */
data class ScaffholdComponents(
    val topBar: @Composable () -> Unit = {},
    val bottomBar: @Composable () -> Unit = {},
    val snackbarHost: @Composable () -> Unit = {},
    val floatingActionButton: @Composable (() -> Unit) = {},
    val floatingActionButtonPosition: FabPosition = FabPosition.End
)