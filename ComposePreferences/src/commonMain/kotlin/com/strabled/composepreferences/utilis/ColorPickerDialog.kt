package com.strabled.composepreferences.utilis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp


/**
 * A [Composable] function that displays a [Color] picker [PreferenceDialog].
 *
 * This function creates a [PreferenceDialog] that allows the user to pick a [Color] using a color wheel.
 * The selected [Color] is displayed in a pill-shaped box and can be confirmed or dismissed.
 * The text displayed in the pill-shaped box can be customized using [DialogText.message] and the [format] style with conversions from the `integral` or `floating point` category or a `string` converion to ingerate the color value.
 * The `argument_index` may be used to get the color value multiple times, but only the `argument_index` 1 can be used ([format] has only one argument, the color value).
 *
 * @param dialogText The text to be displayed in the [PreferenceDialog], including title, dismiss button text, and confirm button text.
 * @param onDismiss The callback to be invoked when the [PreferenceDialog] is dismissed.
 * @param initialColor The initial [Color] selected in the color picker. Defaults to white.
 * @param onColorSelected The callback to be invoked when a [Color] is selected.
 *
 * @see format
 */
@OptIn(ExperimentalStdlibApi::class)
@Composable
fun ColorPickerDialog(
    dialogText: DialogText,
    onDismiss: () -> Unit,
    initialColor: Color = Color.White,
    onColorSelected: (Color) -> Unit
) {
    var selectedColor by remember { mutableStateOf(initialColor) }

    PreferenceDialog(
        title = dialogText.title,
        dismissButton = DialogButton(
            text = dialogText.dismissButton,
            onClick = onDismiss,
        ),
        confirmButton = DialogButton(
            text = dialogText.confirmButton,
            onClick = {
                onColorSelected(selectedColor)
            },
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            ColorWheelPicker(
                initialColor = initialColor,
                onColorSelected = { color ->
                    selectedColor = color
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(modifier = Modifier.fillMaxWidth(0.9f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(dialogText.message != null) {
                        Text(
                            modifier = Modifier.alpha(0.8f),
                            text = if (intRegex.containsMatchIn(dialogText.message)) {
                                dialogText.message.format(selectedColor.toArgb())
                            } else if (stringRegex.containsMatchIn(dialogText.message)) {
                                dialogText.message.format(selectedColor.toArgb().toHexString())
                            } else dialogText.message,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Text(
                            text = "#${selectedColor.toArgb().toHexString(HexFormat.UpperCase)}"
                        )
                    }
                    PillShape(
                        color = selectedColor,
                        modifier = Modifier
                            .width(70.dp)
                            .height(30.dp)
                    )
                }
            }
        }
    }
}

/**
 * A [Composable] function that displays a pill-shaped [Color] box.
 *
 * This function creates a box with rounded corners and a background color specified by the [color] parameter.
 * The [modifier] parameter can be used to apply additional styling to the box.
 *
 * @param color The [Color] to be displayed in the pill shape.
 * @param modifier The [Modifier] to be applied to the pill shape. Defaults to an empty [Modifier].
 */
@Composable
fun PillShape(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
            .background(color)
    )
}

private val intRegex = """%(1\$)?([-#+O, (]+)?(\d+)?(\.\d+)?([doxXeEfgGaA])""".toRegex()

private fun String.format(color: Int): String {
    val matches = intRegex.findAll(this).toList().reversed()
    var returnString = this
    matches.forEach {
        returnString = returnString.replaceRange(it.range, color.toString())
    }
    return returnString
}

private val stringRegex = """(%(1\$)?([-#+O, (]+)?(\d+)?(\.\d+)?([sS]))""".toRegex()

private fun String.format(color: String): String {
    val matches = stringRegex.findAll(this).toList().reversed()
    var returnString = this
    matches.forEach {
        returnString = returnString.replaceRange(it.range, color)
    }
    return returnString
}