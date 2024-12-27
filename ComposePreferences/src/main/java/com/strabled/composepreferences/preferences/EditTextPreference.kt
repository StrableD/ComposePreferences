package com.strabled.composepreferences.preferences

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.strabled.composepreferences.utilis.DialogButton
import com.strabled.composepreferences.utilis.DialogText
import com.strabled.composepreferences.utilis.Preference
import com.strabled.composepreferences.utilis.PreferenceDialog

/**
 * A composable function that represents an editable text preference.
 *
 * This function displays a text preference that can be edited by the user. When the preference is clicked,
 * a dialog is shown with an [OutlinedTextField] for the user to input a new value. The new value is saved
 * when the confirm button is clicked.
 *
 * @param preference The [Preference] object to be edited.
 * @param title The title of the preference.
 * @param modifier The [Modifier] to be applied to the preference.
 * @param summary An optional composable function to display a summary.
 * @param enabled Whether the preference is enabled.
 * @param darkenOnDisable Whether to darken the preference when disabled.
 * @param leadingIcon An optional composable function to display a leading icon.
 * @param dialogText The [DialogText] to be displayed in the dialog.
 * @param onValueChange A callback function to be invoked when the value changes.
 * @param onValueSaved A callback function to be invoked when the value is saved.
 *
 * Example usage:
 * ```
 * EditTextPreference(
 *     preference = myPreference,
 *     title = "My Preference",
 *     onValueChange = { newValue -> /* handle value change */ },
 *     onValueSaved = { newValue -> /* handle value saved */ }
 * )
 * ```
 *
 * @see Preference
 * @see PreferenceDialog
 * @see OutlinedTextField
 */
@Composable
fun EditTextPreference(
    preference: Preference<String>,
    title: String,
    modifier: Modifier = Modifier,
    summary: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    dialogText: DialogText = DialogText(title = title),
    onValueChange: (String) -> Unit = {},
    onValueSaved: (String) -> Unit = {}
) {
    val preferenceValue by preference.collectState()

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(preferenceValue) }

    /**
     * Edits the preference value and handles any exceptions.
     */
    fun edit() {
        try {
            preference.updateValue(textValue)
            onValueChange(textValue)
        } catch (e: Exception) {
            Log.e("EditTextPreference", "Could not write preference $preference to database.", e)
        }
    }

    TextPreference(
        title = title,
        modifier = modifier,
        summary = summary,
        enabled = enabled,
        darkenOnDisable = darkenOnDisable,
        leadingIcon = leadingIcon,
        onClick = { showDialog = enabled }
    )

    if (showDialog) {
        LaunchedEffect(null) {
            textValue = preferenceValue
        }

        PreferenceDialog(
            title = dialogText.title,
            confirmButton = DialogButton(
                text = dialogText.confirmButton,
                onClick = {
                    edit()
                    onValueSaved(textValue)
                    showDialog = false
                },
            ),
            dismissButton = DialogButton(
                text = dialogText.dismissButton,
                onClick = { showDialog = false },
            ),
        ) {
            if (dialogText.message != null) {
                Text(text = dialogText.message)
            }
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true
            )
        }
    }
}