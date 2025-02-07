package com.strabled.composepreferences.preferences

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
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
import com.strabled.composepreferences.PreferenceItem
import com.strabled.composepreferences.utilis.DialogButton
import com.strabled.composepreferences.utilis.DialogText
import com.strabled.composepreferences.utilis.Preference
import com.strabled.composepreferences.utilis.PreferenceDialog

/**
 * A [Composable] function that displays a [dialog][PreferenceDialog] list [preference][PreferenceItem].
 *
 * This function displays a [Preference] that, when clicked, shows a [dialog][PreferenceDialog] with a list of items.
 * The user can select an item from the list, and the selected item will be saved as the [Preference] value.
 * The [summary] can optionally display the selected value.
 *
 * @param T The type of the [Preference] value.
 * @param preference The [Preference] to be displayed and edited.
 * @param title The title of the [preference][PreferenceItem].
 * @param modifier The modifier to be applied to the [preference][PreferenceItem].
 * @param summary A composable function to display the summary of the [preference][PreferenceItem].
 * @param enabled Whether the [preference][PreferenceItem] is enabled.
 * @param darkenOnDisable Whether to darken the [preference][PreferenceItem] when it is disabled.
 * @param leadingIcon A composable function to display a leading [icon][androidx.compose.material3.Icon].
 * @param dialogText The [text][DialogText] to be displayed in the [dialog][PreferenceDialog].
 * @param items A [Map] of values to be stored in the [preference][Preference] and their corresponding display strings.
 * @param useSelectedInSummary Whether to use the selected value in the [summary].
 * @param onItemSelected A callback to be invoked when an item is selected.
 * @param trailingContent A composable function to display trailing content.
 *
 * Usage:
 * ```
 * DialogListPreference(
 *     preference = myPreference,
 *     title = "Choose an option",
 *     items = mapOf("Option1" to "Option 1", "Option2" to "Option 2"),
 *     onItemSelected = { selectedItem -> /* handle selection */ }
 * )
 * ```
 *
 * @see androidx.compose.runtime.Composable
 * @see androidx.compose.material3.Icon
 * @see androidx.compose.foundation.layout.Row
 */
@Composable
fun <T: Any> DialogListPreference(
    preference: Preference<T>,
    title: String,
    modifier: Modifier = Modifier,
    summary: (@Composable (T?) -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    dialogText: DialogText = DialogText(title),
    items: Map<T, String>,
    useSelectedInSummary: Boolean = false,
    onItemSelected: (T) -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) {
    require(!useSelectedInSummary || summary != null) { "When useSelectedInSummary is true, summary must be provided." }
    val preferenceValue by preference.collectState()

    var showDialog by rememberSaveable { mutableStateOf(false) }

    fun edit(value: T) {
        try {
            preference.updateValue(value)
            showDialog = false
            onItemSelected(value)
        } catch (e: Exception) {
            Logger.e("DialogListPreference", "Could not write preference $preference to database.", e)
        }
    }

    TextPreference(
        title = title,
        modifier = modifier,
        summary = @Composable { if (useSelectedInSummary) summary?.invoke(preferenceValue) else summary?.invoke(null) },
        enabled = enabled,
        darkenOnDisable = darkenOnDisable,
        leadingIcon = leadingIcon,
        trailingContent = trailingContent,
        onClick = { showDialog = true }
    )

    if (showDialog) {
        var selected by remember { mutableStateOf(preferenceValue) }
        PreferenceDialog(
            title = dialogText.title,
            confirmButton = DialogButton(
                text = dialogText.confirmButton,
                onClick = { edit(selected) }
            ),
            dismissButton = DialogButton(
                text = dialogText.dismissButton,
                onClick = { showDialog = false }
            ),
        ) {
            LazyColumn {
                items(items.toList()) { (value, text) ->
                    val isSelected = selected == value
                    val onSelectedChage = { if (!isSelected) selected = value }
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
 * A [Composable] function that displays a dialog list preference.
 *
 * This function displays a [Preference] that, when clicked, shows a dialog with a list of items.
 * The user can select an item from the list, and the selected item will be saved as the [Preference] value.
 * The [summary] can optionally display the selected value.
 *
 * @param T The type of the [Preference] value.
 * @param preference The [Preference] to be displayed and edited.
 * @param title The title of the [preference][PreferenceItem].
 * @param modifier The [Modifier] to be applied to the [preference][PreferenceItem].
 * @param summary A [Composable] function to display the summary of the [preference][PreferenceItem].
 * @param enabled Whether the [preference][PreferenceItem] is enabled.
 * @param darkenOnDisable Whether to darken the [preference][PreferenceItem] when it is disabled.
 * @param leadingIcon A [Composable] function to display a leading [icon][androidx.compose.material3.Icon].
 * @param dialogText The [text][DialogText] to be displayed in the [dialog][PreferenceDialog].
 * @param items A [List] of values to be stored in the [preference][Preference].
 * @param transformToDisplayString A function to transform values of [items] to display strings.
 * @param useSelectedInSummary Whether to use the selected value in the [summary].
 * @param onItemSelected A callback to be invoked when an item is selected.
 * @param trailingContent A [Composable] function to display trailing content.
 *
 * Usage:
 * ```
 * val items = listOf("Option1", "Option2", "Option3")
 * DialogListPreference(
 *     preference = myPreference,
 *     title = "Choose an option",
 *     items = items,
 *     transformToDisplayString = { it },
 *     onItemSelected = { selectedItem -> /* handle selection */ }
 * )
 * ```
 *
 * @see androidx.compose.runtime.Composable
 * @see androidx.compose.material3.Icon
 * @see androidx.compose.foundation.layout.Row
 * @see androidx.compose.foundation.lazy.LazyColumn
 * @see androidx.compose.foundation.selection.selectable
 * @see androidx.compose.material3.RadioButton
 */
@Composable
fun <T: Any> DialogListPreference(
    preference: Preference<T>,
    title: String,
    modifier: Modifier = Modifier,
    summary: (@Composable (T?) -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    dialogText: DialogText = DialogText(title),
    items: List<T>,
    transformToDisplayString: (T) -> String = { it.toString() },
    useSelectedInSummary: Boolean = false,
    onItemSelected: (T) -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) = DialogListPreference(
    preference = preference,
    title = title,
    modifier = modifier,
    summary = summary,
    enabled = enabled,
    darkenOnDisable = darkenOnDisable,
    leadingIcon = leadingIcon,
    trailingContent = trailingContent,
    dialogText = dialogText,
    items = items.associateWith(transformToDisplayString),
    useSelectedInSummary = useSelectedInSummary,
    onItemSelected = onItemSelected
)