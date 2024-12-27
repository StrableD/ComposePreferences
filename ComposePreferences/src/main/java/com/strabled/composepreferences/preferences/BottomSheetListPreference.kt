package com.strabled.composepreferences.preferences

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.strabled.composepreferences.utilis.*

/**
 * A [Composable] function that displays a [preference item][com.strabled.composepreferences.PreferenceItem] which, when clicked, shows a [bottom sheet][PreferenceBottomSheet]
 * with a list of selectable items. The selected item is stored in a [DataStore].
 *
 * @param T The type of the [Preference] value.
 * @param preference The [DataStore preference][Preference] to be managed.
 * @param title The title of the [preference item][PreferenceItem].
 * @param modifier The [Modifier] to be applied to the [preference item][PreferenceItem].
 * @param summary A composable function to display a summary optionaly based on the preference value.
 * @param leadingIcon A composable function to display a leading [icon][androidx.compose.material3.Icon] for the [preference item][PreferenceItem].
 * @param enabled Whether the [preference item][PreferenceItem] is enabled.
 * @param darkenOnDisable Whether to darken the [preference item][PreferenceItem] when disabled.
 * @param bottomSheetTitle The title of the [bottom sheet][PreferenceBottomSheet].
 * @param showTitleDivider Whether to show a [divider][androidx.compose.material3.HorizontalDivider] below the [bottom sheet][PreferenceBottomSheet] [title][bottomSheetTitle].
 * @param items A [Map] of items to be displayed in the [bottom sheet][PreferenceBottomSheet], with keys as values to be stored in the [DataStore][androidx.datastore.core.DataStore] and values as display [String]s.
 * @param useSelectedInSummary Whether to use the selected item in the [summary]. When this is true, the [summary] composable function must be provided.
 * @param onValueChange A callback to be invoked when the [Preference] value changes.
 * @param trailingContent A composable function to display trailing content for the [preference item][PreferenceItem].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: Any> BottomSheetListPreference(
    preference: Preference<T>,
    title: String,
    modifier: Modifier = Modifier,
    summary: @Composable ((T?) -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    bottomSheetTitle: String = title,
    showTitleDivider: Boolean = false,
    items: Map<T, String>,
    useSelectedInSummary: Boolean = false,
    onValueChange: (T) -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
) {
    require(!useSelectedInSummary || summary != null) { "When useSelectedInSummary is true, summary must be provided." }
    val preferenceValue by preference.collectState()

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            bottomSheetState.show()
        } else {
            bottomSheetState.hide()
        }
    }

    fun edit(value: T) {
        try {
            preference.updateValue( value)
            showBottomSheet = false
            onValueChange(value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    TextPreference(
        title = title,
        modifier = modifier,
        summary = @Composable { summary?.invoke(if (useSelectedInSummary) preferenceValue else null) },
        leadingIcon = leadingIcon,
        trailingContent = trailingContent,
        enabled = enabled,
        darkenOnDisable = darkenOnDisable,
        onClick = { showBottomSheet = true }
    )

    if (showBottomSheet) {
        PreferenceBottomSheet(
            title = bottomSheetTitle,
            showTitleDivider = showTitleDivider,
            sheetState = bottomSheetState,
            onDismiss = { showBottomSheet = false }
        ) {
            LazyColumn {
                items(items.toList()) { (value, text) ->
                    val isSelected = preferenceValue == value
                    val onSelectedChage = { if (!isSelected) edit(value) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isSelected,
                                onClick = onSelectedChage,
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = onSelectedChage,
                        )
                        Text(text = text)
                    }
                }
            }
        }
    }
}

/**
 * A [Composable] function that displays a [preference item][com.strabled.composepreferences.PreferenceItem] which, when clicked, shows a [bottom sheet][PreferenceBottomSheet]
 * with a list of selectable items. The selected item is stored in a [DataStore].
 *
 * @param T The type of the [Preference] value.
 * @param preference The [DataStore preference][Preference] to be managed.
 * @param title The title of the [preference item][PreferenceItem].
 * @param modifier The [Modifier] to be applied to the [preference item][PreferenceItem].
 * @param summary A composable function to display a summary optionaly based on the preference value.
 * @param leadingIcon A composable function to display a leading [icon][androidx.compose.material3.Icon] for the [preference item][PreferenceItem].
 * @param enabled Whether the [preference item][PreferenceItem] is enabled.
 * @param darkenOnDisable Whether to darken the [preference item][PreferenceItem] when disabled.
 * @param bottomSheetTitle The title of the [bottom sheet][PreferenceBottomSheet].
 * @param showTitleDivider Whether to show a [divider][androidx.compose.material3.HorizontalDivider] below the [bottom sheet][PreferenceBottomSheet] [title][bottomSheetTitle].
 * @param items A [List] of items to be displayed in the [bottom sheet][PreferenceBottomSheet], with the values to be stored in the [DataStore][androidx.datastore.core.DataStore].
 * @param useSelectedInSummary Whether to use the selected item in the [summary]. When this is true, the [summary] composable function must be provided.
 * @param transformToDisplayString A function to transform the values in [items] to a [String] to be displayed in the [bottom sheet][PreferenceBottomSheet].
 * @param onValueChange A callback to be invoked when the [Preference] value changes.
 * @param trailingContent A composable function to display trailing content for the [preference item][PreferenceItem].
 */
@Composable
fun <T: Any> BottomSheetListPreference(
    preference: Preference<T>,
    title: String,
    modifier: Modifier = Modifier,
    summary: @Composable ((T?) -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    bottomSheetTitle: String = title,
    showTitleDivider: Boolean = false,
    items: List<T>,
    useSelectedInSummary: Boolean = false,
    transformToDisplayString: (T) -> String = { it.toString() },
    onValueChange: (T) -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
) = BottomSheetListPreference(
    preference = preference,
    title = title,
    modifier = modifier,
    summary = summary,
    leadingIcon = leadingIcon,
    enabled = enabled,
    darkenOnDisable = darkenOnDisable,
    bottomSheetTitle = bottomSheetTitle,
    showTitleDivider = showTitleDivider,
    items = items.associateWith(transformToDisplayString),
    useSelectedInSummary = useSelectedInSummary,
    onValueChange = onValueChange,
    trailingContent = trailingContent,
)