package com.strabled.composepreferences.preferences

import android.util.Log
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.strabled.composepreferences.utilis.DataStoreManager

/**
 * A [Composable] function that displays a [Checkbox] [preference item][com.strabled.composepreferences.PreferenceItem].
 *
 * This function uses [DataStoreManager] to manage the preference data and displays a [Checkbox]
 * that reflects the current [Preference] value. When the [Checkbox] is clicked, the [Preference] value
 * is updated and the [onValueChange] callback is invoked.
 *
 * @param preference The [preference][com.strabled.composepreferences.utilis.DataStoreManager.Preference] to be managed.
 * @param title The title of the [preference item][com.strabled.composepreferences.PreferenceItem].
 * @param modifier The [Modifier] to be applied to the [preference item][com.strabled.composepreferences.PreferenceItem].
 * @param summary An optional composable function to display a summary below the [title].
 * @param enabled Whether the [preference item][com.strabled.composepreferences.PreferenceItem] is enabled.
 * @param darkenOnDisable Whether to darken the [preference item][com.strabled.composepreferences.PreferenceItem] when it is disabled.
 * @param leadingIcon An optional composable function to display a leading [icon][androidx.compose.material3.Icon].
 * @param onValueChange A callback function to be invoked when the [Preference] value changes.
 *
 * Example usage:
 * ```
 * CheckBoxPreference(
 *     preferences = myBooleanPreference,
 *     title = "Enable Feature",
 *     summary = { Text("Enable or disable the feature") },
 *     onValueChange = { newValue -> /* handle value change */ }
 * )
 * ```
 *
 * @see DataStoreManager
 * @see androidx.compose.material3.Checkbox
 */
@Composable
fun CheckBoxPreference(
    preference: DataStoreManager.Preference<Boolean>,
    title: String,
    modifier: Modifier = Modifier,
    summary: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (Boolean) -> Unit = {}
) {
    val preferenceData by preference
    val preferenceValue by preferenceData.collectAsState()

    fun edit(newValue: Boolean) {
        try {
            preference.set(newValue)
            onValueChange(newValue)
        } catch (e: Exception) {
            Log.e("CheckBoxPreference", "Could not write preference $preference to database.", e)
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
        Checkbox(
            checked = preferenceValue,
            onCheckedChange = { edit(it) },
            enabled = enabled
        )
    }
}