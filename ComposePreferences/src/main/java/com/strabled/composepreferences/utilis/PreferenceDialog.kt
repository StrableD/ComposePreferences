package com.strabled.composepreferences.utilis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.strabled.composepreferences.PreferenceTheme


/**
 * Displays a preference [dialog][Dialog] with a [title], [confirm button][DialogButton], [dismiss button][DialogButton], and custom content.
 *
 * @param title The title of the [dialog][Dialog].
 * @param confirmButton The [button][DialogButton] to confirm the action.
 * @param dismissButton The [button][DialogButton] to dismiss the dialog.
 * @param content The [Composable] content to be displayed in the dialog.
 *
 * Usage:
 * ```
 * PreferenceDialog(
 *     title = "Dialog Title",
 *     confirmButton = DialogButton("Confirm") { /* Confirm action */ },
 *     dismissButton = DialogButton("Dismiss") { /* Dismiss action */ },
 *     content = { /* Custom content */ }
 * )
 * ```
 */
@Composable
fun PreferenceDialog(
    title: String,
    confirmButton: DialogButton,
    dismissButton: DialogButton,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = dismissButton.onClick,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            colors = CardDefaults.cardColors(containerColor = PreferenceTheme.colorScheme.dialogBackgroundColor, contentColor = PreferenceTheme.colorScheme.dialogContentColor)
        ) {
            Column(
                modifier = Modifier.padding(PreferenceTheme.spacing.dialogPadding),
                verticalArrangement = Arrangement.spacedBy(PreferenceTheme.spacing.dialogContentGap)
            ) {
                Text(
                    text = title,
                    style = PreferenceTheme.typography.dialogTitleStyle,
                    color = PreferenceTheme.colorScheme.dialogTitleColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PreferenceTheme.spacing.dialogTitlePadding),
                )
                ProvideTextStyle(PreferenceTheme.typography.dialogContentStyle) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(PreferenceTheme.spacing.dialogContentPadding),
                        verticalArrangement = PreferenceTheme.spacing.dialogContentArrangement,
                        content = content
                    )
                }
                Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    dismissButton.render()
                    confirmButton.render(primaryAction = true)
                }
            }
        }
    }
}


/**
 * Represents a [button][Button] in the [dialog][PreferenceDialog] with [text] and an [onClick] action.
 *
 * @property text The text to be displayed on the [button][Button].
 * @property onClick The action to be performed when the [button][DialogButton] is clicked.
 *
 * Usage:
 * ```
 * val confirmButton = DialogButton("Confirm") { /* Confirm action */ }
 * val dismissButton = DialogButton("Dismiss") { /* Dismiss action */ }
 * ```
 */
data class DialogButton(
    private val text: String,
    internal val onClick: () -> Unit,
) {
    /**
     * Renders the [button][DialogButton] as a [primary action button][Button] or a [text button][TextButton].
     *
     * @param primaryAction If true, renders as a [primary action button][Button]; otherwise, renders as a [text button][TextButton].
     *
     * Usage:
     * ```
     * confirmButton.render(primaryAction = true)
     * dismissButton.render()
     * ```
     */
    @Composable
    internal fun render(primaryAction: Boolean = false) {
        if (primaryAction) {
            Button(
                modifier = Modifier.padding(PreferenceTheme.spacing.dialogButtonPadding),
                onClick = onClick,
            ) {
                Text(text = text, style = PreferenceTheme.typography.dialogContentStyle)
            }
        } else {
            TextButton(
                modifier = Modifier.padding(PreferenceTheme.spacing.dialogButtonPadding),
                onClick = onClick
            ) {
                Text(text = text, style = PreferenceTheme.typography.dialogContentStyle.copy(color = PreferenceTheme.colorScheme.dialogContentColor))
            }
        }
    }

}

/**
 * Represents the text content of a [dialog][PreferenceDialog], including [title], [message], and [button][DialogButton] [texts][DialogButton.text].
 *
 * @property title The title of the [dialog][PreferenceDialog].
 * @property message The message content of the [dialog][PreferenceDialog] (optional).
 * @property confirmButton The text for the [confirm button][DialogButton] (default is "Save").
 * @property dismissButton The text for the [dismiss button][DialogButton] (default is "Cancel").
 *
 * Usage:
 * ```
 * val dialogText = DialogText(
 *     title = "Dialog Title",
 *     message = "Dialog message",
 *     confirmButton = "Confirm",
 *     dismissButton = "Dismiss"
 * )
 * ```
 */
data class DialogText(
    val title: String,
    val message: String? = null,
    val confirmButton: String = "Save",
    val dismissButton: String = "Cancel",
)