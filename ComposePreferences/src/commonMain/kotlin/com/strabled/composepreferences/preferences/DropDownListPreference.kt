package com.strabled.composepreferences.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.strabled.composepreferences.Logger
import com.strabled.composepreferences.PreferenceTheme
import com.strabled.composepreferences.utilis.Preference

/**
 * A [Composable][androidx.compose.runtime.Composable] function that displays a dropdown list preference.
 *
 * This function displays a dropdown list preference that allows users to select an item from a list.
 * The selected item is stored in the provided [preference][Preference] object.
 * The function also provides customization options for the [title], [summary], [icon][leadingIcon], and more.
 *
 * @param T The type of the preference value.
 * @param preference The [preference][Preference] object to manage the selected value.
 * @param title The title of the preference.
 * @param modifier The [modifier][androidx.compose.ui.Modifier] to be applied to the preference layout.
 * @param summary A [Composable][androidx.compose.runtime.Composable] function to display the summary of the preference.
 * @param enabled Whether the preference is enabled.
 * @param darkenOnDisable Whether to darken the preference when disabled.
 * @param leadingIcon A [Composable][androidx.compose.runtime.Composable] function to display a leading icon.
 * @param items A map of items to display in the dropdown menu, where the key is the value and the value is the display string.
 * @param useSelectedInSummary Whether to use the selected item in the summary.
 * @param onItemSelected A callback function to be invoked when an item is selected.
 * @param trailingContent A [Composable][androidx.compose.runtime.Composable] function to display trailing content.
 *
 * Usage:
 * ```
 * val items = mapOf("Option1" to "Option 1", "Option2" to "Option 2")
 * DropDownListPreference(
 *     preference = myPreference,
 *     title = "Choose an option",
 *     items = items,
 *     onItemSelected = { selectedItem -> /* handle selection */ }
 * )
 * ```
 *
 * Another example with more customization:
 * ```
 * val items = mapOf(1 to "One", 2 to "Two", 3 to "Three")
 * DropDownListPreference(
 *     preference = myIntPreference,
 *     title = "Select a number",
 *     summary = { selectedValue -> Text("Selected: $selectedValue") },
 *     leadingIcon = { Icon(Icons.Default.List, contentDescription = null) },
 *     items = items,
 *     useSelectedInSummary = true,
 *     onItemSelected = { selectedItem -> Log.d("Preference", "Selected: $selectedItem") },
 *     trailingContent = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
 * )
 * ```
 */
@Composable
fun <T: Any> DropDownListPreference(
    preference: Preference<T>,
    title: String,
    modifier: Modifier = Modifier,
    summary: (@Composable (T?) -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    items: Map<T, String>,
    useSelectedInSummary: Boolean = false,
    onItemSelected: (T) -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) {
    require(!useSelectedInSummary || summary != null) { "Summary must be provided when useSelectedInSummary is true" }
    val preferenceValue by preference.collectState()

    var expanded by rememberSaveable { mutableStateOf(false) }

    fun edit(value: T) {
        try {
            preference.updateValue(value)
            expanded = false
            onItemSelected(value)
        } catch (e: Exception) {
            Logger.e("DropDownListPreference", "Could not write preference $preference to database.", e)
        }
    }

    Column(horizontalAlignment = PreferenceTheme.spacing.dropdownAlignment) {
        TextPreference(
            title = title,
            modifier = modifier,
            summary = @Composable { if (useSelectedInSummary) summary?.invoke(preferenceValue) else summary?.invoke(null) },
            enabled = enabled,
            darkenOnDisable = darkenOnDisable,
            leadingIcon = leadingIcon,
            trailingContent = trailingContent,
            onClick = { if (enabled) expanded = true }
        )
        Box(modifier = Modifier.padding(horizontal = PreferenceTheme.spacing.dropdownOffset)) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = PreferenceTheme.colorScheme.dropdownBackgroundColor,
                modifier = Modifier.padding(PreferenceTheme.spacing.dropdownPadding)
            ) {
                ProvideTextStyle(PreferenceTheme.typography.dropdownContentStyle) {
                    items.forEach { (value, text) ->
                        DropdownMenuItem(
                            onClick = { edit(value) },
                            text = {
                                Text(text = text)
                            },
                            colors = MenuDefaults.itemColors(PreferenceTheme.colorScheme.dropdownContentColor),
                            contentPadding = PreferenceTheme.spacing.dropdownContentPadding
                        )
                    }
                }
            }
        }
    }
}

/**
 * A [composable][androidx.compose.runtime.Composable] function that displays a dropdown list preference.
 *
 * This function displays a dropdown list preference that allows users to select an item from a list.
 * The selected item is stored in the provided [preference](androidx.datastore.preferences.core.Preference) object.
 * The function also provides customization options for the [title], [summary], [icons][androidx.compose.material3.Icon], and more.
 *
 * @param T The type of the preference value.
 * @param preference The [preference][Preference] object to manage the selected value.
 * @param title The title of the preference.
 * @param modifier The [modifier][androidx.compose.ui.Modifier] to be applied to the preference layout.
 * @param summary A [composable][androidx.compose.runtime.Composable] function to display the summary of the preference.
 * @param enabled Whether the preference is enabled.
 * @param darkenOnDisable Whether to darken the preference when disabled.
 * @param leadingIcon A [composable][androidx.compose.runtime.Composable] function to display a leading icon.
 * @param items A list of items to display in the dropdown menu.
 * @param useSelectedInSummary Whether to use the selected item in the summary.
 * @param transformToDisplayString A function to transform the item to a display string.
 * @param onItemSelected A callback function to be invoked when an item is selected.
 * @param trailingContent A [composable][androidx.compose.runtime.Composable] function to display trailing content.
 *
 * Usage:
 * ```
 * val items = listOf("Option1", "Option2", "Option3")
 * DropDownListPreference(
 *     preference = myPreference,
 *     title = "Choose an option",
 *     items = items,
 *     transformToDisplayString = { it },
 *     onItemSelected = { selectedItem -> /* handle selection */ }
 * )
 * ```
 *
 * Another example with more customization:
 * ```
 * val items = listOf(1, 2, 3)
 * DropDownListPreference(
 *     preference = myIntPreference,
 *     title = "Select a number",
 *     summary = { selectedValue -> Text("Selected: $selectedValue") },
 *     leadingIcon = { Icon(Icons.Default.List, contentDescription = null) },
 *     items = items,
 *     useSelectedInSummary = true,
 *     transformToDisplayString = { it.toString() },
 *     onItemSelected = { selectedItem -> Log.d("Preference", "Selected: $selectedItem") },
 *     trailingContent = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
 * )
 * ```
 */
@Composable
fun <T: Any> DropDownListPreference(
    preference: Preference<T>,
    title: String,
    modifier: Modifier = Modifier,
    summary: (@Composable (T?) -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    items: List<T>,
    useSelectedInSummary: Boolean = false,
    transformToDisplayString: (T) -> String = { it.toString() },
    onItemSelected: (T) -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) = DropDownListPreference(
    preference = preference,
    title = title,
    modifier = modifier,
    summary = summary,
    enabled = enabled,
    darkenOnDisable = darkenOnDisable,
    leadingIcon = leadingIcon,
    trailingContent = trailingContent,
    items = items.associateWith(transformToDisplayString),
    useSelectedInSummary = useSelectedInSummary,
    onItemSelected = onItemSelected
)