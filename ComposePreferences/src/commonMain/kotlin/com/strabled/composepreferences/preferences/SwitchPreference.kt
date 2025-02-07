package com.strabled.composepreferences.preferences

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.strabled.composepreferences.Logger
import com.strabled.composepreferences.utilis.DataStoreManager
import com.strabled.composepreferences.utilis.Preference

/**
 * A [Composable] function that represents a switch preference.
 *
 * This function uses [DataStoreManager] to manage the preference data and displays a [Switch] component.
 * The [TextPreference] composable is used to display the [title], [summary], and [leading icon][leadingIcon].
 * When the switch is toggled, the new value is saved and the [onValueChange] callback is invoked.
 *
 * @param preference The preference object that holds the Boolean value.
 * @param title The title of the preference.
 * @param modifier The modifier to be applied to the preference.
 * @param summary An optional composable function to display a summary below the title.
 * @param enabled Whether the preference is enabled or not.
 * @param darkenOnDisable Whether to darken the preference when it is disabled.
 * @param leadingIcon An optional composable function to display a leading icon.
 * @param onValueChange A callback function to be invoked when the preference value changes.
 *
 * Example usage:
 * ```
 * val preference = Preference<Boolean>("example_key", false)
 * SwitchPreference(
 *     preference = preference,
 *     title = "Example Switch",
 *     onValueChange = { newValue -> println("New value: $newValue") }
 * )
 * ```
 */
@Composable
fun SwitchPreference(
    preference: Preference<Boolean>,
    title: String,
    modifier: Modifier = Modifier,
    summary: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (Boolean) -> Unit = {}
) {
    val preferenceValue by  preference.collectState()

    fun edit(newValue: Boolean) {
        try {
            preference.updateValue(newValue)
            onValueChange(newValue)
        } catch (e: Exception) {
            Logger.e("SwitchPreference", "Could not write preference $preference to database.", e)
        }
    }

    TextPreference(
        title = title,
        modifier = modifier,
        summary = summary,
        enabled = enabled,
        darkenOnDisable = darkenOnDisable,
        leadingIcon = leadingIcon,
        onClick = { edit(!preferenceValue) }
    ) {
        Switch(
            checked = preferenceValue,
            onCheckedChange = { edit(it) },
            enabled = enabled
        )
    }
}