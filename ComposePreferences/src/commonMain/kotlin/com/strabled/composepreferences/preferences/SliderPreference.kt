package com.strabled.composepreferences.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.strabled.composepreferences.Logger
import com.strabled.composepreferences.PreferenceTheme
import com.strabled.composepreferences.utilis.Preference

/**
 * A composable function that displays a [Slider] preference.
 *
 * @param preference The [Preference] object to be managed.
 * @param title The title of the preference.
 * @param modifier The [Modifier] to be applied to the preference layout.
 * @param summary An optional composable function to display a summary below the title.
 * @param enabled Whether the slider is enabled or not.
 * @param darkenOnDisable Whether to darken the slider when it is disabled.
 * @param leadingIcon An optional composable function to display a leading icon.
 * @param onValueChange A callback function to be invoked when the slider value changes.
 * @param valueRange The range of values the slider can take.
 * @param steps The number of discrete steps the slider can take between the min and max values.
 * @param showValue Whether to show the current value of the slider next to it.
 * @param T The type of the preference value, which must be a number and comparable.
 *
 * Example usage:
 * ```
 * SliderPreference(
 *     preference = myPreference,
 *     title = "Volume",
 *     valueRange = 0f..100f,
 *     steps = 10,
 *     showValue = true,
 *     onValueChange = { newValue -> /* handle value change */ }
 * )
 * ```
 *
 * @see [Preference]
 * @see [Modifier]
 * @see [Composable]
 */
@Composable
fun <T> SliderPreference(
    preference: Preference<T>,
    title: String,
    modifier: Modifier = Modifier,
    summary: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (T) -> Unit = {},
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    showValue: Boolean = false
) where T : Number, T : Comparable<T> {
    val preferenceValue by preference.collectState()

    var value by remember { mutableFloatStateOf(preferenceValue.toFloat()) }

    LaunchedEffect(preferenceValue) {
        value = preferenceValue.toFloat()
    }

    /**
     * Edits the preference value and updates the datastore.
     *
     * @param newValue The new value to be set.
     */
    fun edit(newValue: T) {
        try {
            preference.updateValue(newValue)
            onValueChange(newValue)
        } catch (e: Exception) {
            Logger.e("SliderPreference", "Could not write preference $preference to database.", e)
        }
    }

    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        TextPreference(
            title = title,
            modifier = modifier,
            summary = summary,
            enabled = false,
            darkenOnDisable = darkenOnDisable,
            leadingIcon = leadingIcon,
            makeMinimal = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PreferenceTheme.spacing.sliderPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Slider(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier
                    .weight(2.1f),
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = { @Suppress("UNCHECKED_CAST") edit(value as T) },
                enabled = enabled
            )

            if (showValue) {
                val roundedValue = when (preferenceValue) {
                    is Float, is Double -> value.toString().apply {
                        val index = indexOf('.')
                        if (index != -1 && length >= index + 2) {
                            substring(0, index + 2)
                        }
                    }

                    else -> value.toInt().toString()
                }
                Text(
                    text = roundedValue,
                    color = PreferenceTheme.colorScheme.trailingContentColor,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(0.4f)
                )
            }
        }
    }
}