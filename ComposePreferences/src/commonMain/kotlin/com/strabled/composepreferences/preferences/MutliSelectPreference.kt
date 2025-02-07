package com.strabled.composepreferences.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.strabled.composepreferences.Logger
import com.strabled.composepreferences.utilis.DialogButton
import com.strabled.composepreferences.utilis.DialogText
import com.strabled.composepreferences.utilis.Preference
import com.strabled.composepreferences.utilis.PreferenceDialog

/**
 * A [Composable] function that displays a multi-select preference item.
 *
 * This function displays a preference item that allows the user to select multiple items from a list.
 * When the preference item is clicked, a dialog is shown with a list of items to select from.
 * The selected items are stored in the provided [preference] object.
 *
 * @param preference The [Preference] object that holds the set of selected items.
 * @param title The title of the preference item.
 * @param modifier The [Modifier] to be applied to the preference item.
 * @param summary An optional [Composable] function to display a summary of the selected items.
 * @param enabled Whether the preference item is enabled.
 * @param darkenOnDisable Whether to darken the preference item when disabled.
 * @param leadingIcon An optional [Composable] function to display a leading icon.
 * @param dialogText The [DialogText] to be displayed in the dialog.
 * @param useSelectedInSummary Whether to use the selected items in the summary.
 * @param items A map of item values to their display text.
 * @param onItemSelected A callback function to be invoked when an item is selected.
 * @param trailingContent An optional [Composable] function to display trailing content.
 *
 * Example usage:
 * ```
 * val myPreference = Preference<Set<String>>("my_preference_key")
 *
 * MultiSelectPreference(
 *     preference = myPreference,
 *     title = "Select Items",
 *     items = mapOf("item1" to "Item 1", "item2" to "Item 2"),
 *     onItemSelected = { selectedItems ->
 *         // handle selected items
 *     }
 * )
 * ```
 */
@Composable
fun MultiSelectPreference(
    preference: Preference<Set<String>>,
    title: String,
    modifier: Modifier = Modifier,
    summary: (@Composable (Set<String>) -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    dialogText: DialogText = DialogText(title),
    useSelectedInSummary: Boolean = false,
    items: Map<String, String>,
    onItemSelected: (Set<String>) -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) {
    require(!useSelectedInSummary || summary != null) { "Summary must be provided when useSelectedInSummary is true" }
    val preferenceValue by preference.collectState()

    var showDialog by rememberSaveable { mutableStateOf(false) }

    fun edit(newValue: Set<String>) {
        try {
            preference.updateValue(newValue)
            showDialog = false
            onItemSelected(newValue)
        } catch (e: Exception) {
            Logger.e("MultiSelectPreference", "Could not write preference $preference to database.", e)
        }
    }

    TextPreference(
        title = title,
        modifier = modifier,
        summary = @Composable { if (useSelectedInSummary) summary?.invoke(preferenceValue) else summary?.invoke(setOf()) },
        enabled = enabled,
        darkenOnDisable = darkenOnDisable,
        leadingIcon = leadingIcon,
        trailingContent = trailingContent,
        onClick = { if (enabled) showDialog = true }
    )

    if (showDialog) {
        var selected by remember { mutableStateOf(preferenceValue.toSet()) }
        PreferenceDialog(
            title = dialogText.title,
            confirmButton = DialogButton(
                text = dialogText.confirmButton,
                onClick = {
                    edit(selected)
                }
            ),
            dismissButton = DialogButton(
                text = dialogText.dismissButton,
                onClick = { showDialog = false }
            ),
        ) {
            LazyColumn {
                items(items.toList()) { (value, text) ->
                    val isSelected = selected.contains(value)
                    val onSelectedChage: (Boolean) -> Unit = { selected = if (isSelected) selected - value else selected + value }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = { onSelectedChage(true) },
                                role = Role.Checkbox
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = onSelectedChage,
                            enabled = enabled,
                        )
                        Text(
                            text = text
                        )
                    }
                }
            }
        }
    }
}

/**
 * A [Composable] function that displays a multi-select preference item.
 *
 * This function displays a preference item that allows the user to select multiple items from a list.
 * When the preference item is clicked, a dialog is shown with a list of items to select from.
 * The selected items are stored in the provided [preference][Preference] object.
 *
 * @param preference The [Preference] object that holds the set of selected items.
 * @param title The title of the preference item.
 * @param modifier The [Modifier] to be applied to the preference item.
 * @param summary An optional [Composable] function to display a summary of the selected items.
 * @param enabled Whether the preference item is enabled.
 * @param darkenOnDisable Whether to darken the preference item when disabled.
 * @param leadingIcon An optional [Composable] function to display a leading icon.
 * @param dialogText The [DialogText] to be displayed in the dialog.
 * @param useSelectedInSummary Whether to use the selected items in the summary.
 * @param items A list of item values.
 * @param transformToDisplayString A function to transform item values to their display text.
 * @param onItemSelected A callback function to be invoked when an item is selected.
 * @param trailingContent An optional [Composable] function to display trailing content.
 *
 * Example usage:
 * ```
 * val myPreference = Preference<Set<String>>("my_preference_key")
 *
 * MultiSelectPreference(
 *     preference = myPreference,
 *     title = "Select Items",
 *     items = listOf("item1", "item2"),
 *     transformToDisplayString = { it.toUpperCase() },
 *     onItemSelected = { selectedItems ->
 *         // handle selected items
 *     }
 * )
 * ```
 *
 * @see [Preference]
 * @see [Modifier]
 * @see [Composable]
 */
@Composable
fun MultiSelectPreference(
    preference: Preference<Set<String>>,
    title: String,
    modifier: Modifier = Modifier,
    summary: (@Composable (Set<String>) -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    dialogText: DialogText = DialogText(title),
    useSelectedInSummary: Boolean = false,
    items: List<String>,
    transformToDisplayString: (String) -> String = { it },
    onItemSelected: (Set<String>) -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) = MultiSelectPreference(
    preference = preference,
    title = title,
    modifier = modifier,
    summary = summary,
    enabled = enabled,
    darkenOnDisable = darkenOnDisable,
    leadingIcon = leadingIcon,
    trailingContent = trailingContent,
    dialogText = dialogText,
    useSelectedInSummary = useSelectedInSummary,
    items = items.associateWith { transformToDisplayString(it) },
    onItemSelected = onItemSelected
)