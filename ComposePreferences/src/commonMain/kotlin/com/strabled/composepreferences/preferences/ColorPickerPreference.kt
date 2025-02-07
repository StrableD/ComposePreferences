package com.strabled.composepreferences.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.strabled.composepreferences.Logger
import com.strabled.composepreferences.PreferenceItem
import com.strabled.composepreferences.utilis.ColorPickerDialog
import com.strabled.composepreferences.utilis.DialogText
import com.strabled.composepreferences.utilis.Preference
import kotlin.jvm.JvmName

/**
 * A [Composable] function that displays a [color picker][ColorPickerDialog] [preference][PreferenceItem].
 *
 * @param T The type of the [Preference] value, which must be a [Long] or a [Int].
 * @param preference The [Preference] object to store the selected [Color].
 * @param title The title of the [preference][PreferenceItem].
 * @param modifier The [Modifier] to be applied to the [preference][PreferenceItem].
 * @param summary A composable function to display the summary of the [preference][PreferenceItem] using the [selected][Color] value.
 * @param enabled Whether the [preference][PreferenceItem] is enabled.
 * @param darkenOnDisable Whether to darken the [preference][PreferenceItem] when it is disabled.
 * @param leadingIcon A composable function to display a leading [icon][androidx.compose.material3.Icon].
 * @param dialogText The text to be displayed in the [color picker dialog][ColorPickerDialog].
 * @param useSelectedInSummary Whether to use the selected color in the [summary].
 * @param onColorSelected A callback function to be invoked when a [Color] is selected.
 * @param trailingContent A composable function to display trailing content.
 * @throws IllegalArgumentException If the type of the preference is not supported. Supporte types: [Int], [Long].
 */
@Composable
@Deprecated("Use ColorPickerPreference instead", ReplaceWith("ColorPickerPreference(preference, title, modifier, summary, enabled, darkenOnDisable, leadingIcon, dialogText, useSelectedInSummary, onColorSelected, trailingContent)"), DeprecationLevel.HIDDEN)
inline fun <reified T> ColorPickerPreference(
    preference: Preference<T>,
    title: String,
    modifier: Modifier = Modifier,
    noinline summary: (@Composable (Color?) -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    noinline leadingIcon: @Composable (() -> Unit)? = null,
    dialogText: DialogText = DialogText(title),
    useSelectedInSummary: Boolean = false,
    noinline onColorSelected: (Color) -> Unit = {},
    noinline trailingContent: @Composable () -> Unit = {}
) where T : Number, T : Comparable<T> =
    @Suppress("UNCHECKED_CAST")
    when (T::class) {
        Int::class -> IntColorPickerDialog(
            preference = preference as Preference<Int>,
            title = title,
            modifier = modifier,
            summary = summary,
            enabled = enabled,
            darkenOnDisable = darkenOnDisable,
            leadingIcon = leadingIcon,
            trailingContent = trailingContent,
            dialogText = dialogText,
            useSelectedInSummary = useSelectedInSummary,
            onColorSelected = onColorSelected
        )

        Long::class -> LongColorPickerDialog(
            preference = preference as Preference<Long>,
            title = title,
            modifier = modifier,
            summary = summary,
            enabled = enabled,
            darkenOnDisable = darkenOnDisable,
            leadingIcon = leadingIcon,
            trailingContent = trailingContent,
            dialogText = dialogText,
            useSelectedInSummary = useSelectedInSummary,
            onColorSelected = onColorSelected
        )

        else -> throw IllegalArgumentException("Unsupported type for ColorPickerPreference: ${T::class.simpleName}")
    }


/**
 * A [Composable] function that displays a [color picker dialog][ColorPickerDialog] for [integer][Int] [Preference]s.
 *
 * @param preference The [Preference] object to store the selected [Color].
 * @param title The title of the [preference][PreferenceItem].
 * @param modifier The [Modifier] to be applied to the [preference][PreferenceItem].
 * @param summary A composable function to display the summary of the [preference][PreferenceItem] using the [selected][Color] value.
 * @param enabled Whether the [preference][PreferenceItem] is enabled.
 * @param darkenOnDisable Whether to darken the [preference][PreferenceItem] when it is disabled.
 * @param leadingIcon A composable function to display a leading [icon][androidx.compose.material3.Icon].
 * @param dialogText The text to be displayed in the [color picker dialog][ColorPickerDialog].
 * @param useSelectedInSummary Whether to use the selected color in the [summary].
 * @param onColorSelected A callback function to be invoked when a [Color] is selected.
 * @param trailingContent A composable function to display trailing content.
 */
@Composable
@PublishedApi
internal fun IntColorPickerDialog(
    preference: Preference<Int>,
    title: String,
    modifier: Modifier = Modifier,
    summary: (@Composable (Color?) -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    dialogText: DialogText = DialogText(title),
    useSelectedInSummary: Boolean = false,
    onColorSelected: (Color) -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) {
    require(!useSelectedInSummary || summary != null) { "When useSeletcedInSummary is true, summary must be provided." }
    val preferenceValue by preference.collectState()
    val color = Color(preferenceValue)

    var showDialog by rememberSaveable { mutableStateOf(false) }

    fun edit(color: Color) {
        try {
            preference.updateValue(color.toArgb())
            showDialog = false
            onColorSelected(color)
        } catch (e: Exception) {
            Logger.e("ColorPickerPreference", "Could not write preference $preference to database.", e)
        }
    }

    TextPreference(
        title = title,
        modifier = modifier,
        summary = @Composable { if (useSelectedInSummary) summary?.invoke(color) else summary?.invoke(null) },
        enabled = enabled,
        darkenOnDisable = darkenOnDisable,
        leadingIcon = leadingIcon,
        trailingContent = trailingContent,
        onClick = { showDialog = true }
    )

    if (showDialog) {
        ColorPickerDialog(
            dialogText = dialogText,
            onDismiss = { showDialog = false },
            initialColor = color,
            onColorSelected = ::edit
        )
    }
}

/**
 * A [Composable] function that displays a [color picker dialog][ColorPickerDialog] for [Long] [Preference]s.
 *
 * @param preference The [Preference] object to store the selected [Color].
 * @param title The title of the [preference][PreferenceItem].
 * @param modifier The [Modifier] to be applied to the [preference][PreferenceItem].
 * @param summary A composable function to display the summary of the [preference][PreferenceItem] using the [selected][Color] value.
 * @param enabled Whether the [preference][PreferenceItem] is enabled.
 * @param darkenOnDisable Whether to darken the [preference][PreferenceItem] when it is disabled.
 * @param leadingIcon A composable function to display a leading [icon][androidx.compose.material3.Icon].
 * @param dialogText The text to be displayed in the [color picker dialog][ColorPickerDialog].
 * @param useSelectedInSummary Whether to use the selected color in the [summary].
 * @param onColorSelected A callback function to be invoked when a [Color] is selected.
 * @param trailingContent A composable function to display trailing content.
 */
@Composable
@PublishedApi
internal fun LongColorPickerDialog(
    preference: Preference<Long>,
    title: String,
    modifier: Modifier = Modifier,
    summary: (@Composable (Color?) -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    dialogText: DialogText = DialogText(title),
    useSelectedInSummary: Boolean = false,
    onColorSelected: (Color) -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) {
    require(!useSelectedInSummary || summary != null) { "When useSeletcedInSummary is true, summary must be provided." }
    val preferenceValue by preference.collectState()
    val color = Color(preferenceValue)

    var showDialog by rememberSaveable { mutableStateOf(false) }

    fun edit(color: Color) {
        try {
            preference.updateValue(color.toArgb().toLong())
            showDialog = false
            onColorSelected(color)
        } catch (e: Exception) {
            Logger.e("ColorPickerPreference", "Could not write preference $preference to database.", e)
        }
    }

    TextPreference(
        title = title,
        modifier = modifier,
        summary = @Composable { if (useSelectedInSummary) summary?.invoke(color) else summary?.invoke(null) },
        enabled = enabled,
        darkenOnDisable = darkenOnDisable,
        leadingIcon = leadingIcon,
        trailingContent = trailingContent,
        onClick = { showDialog = true }
    )

    if (showDialog) {
        ColorPickerDialog(
            dialogText = dialogText,
            onDismiss = { showDialog = false },
            initialColor = color,
            onColorSelected = ::edit
        )
    }
}

@JvmName("ColorPickerPreferenceColor")
@Composable
fun ColorPickerPreference(
    preference: Preference<Color>,
    title: String,
    modifier: Modifier = Modifier,
    summary: (@Composable (Color?) -> Unit)? = null,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    dialogText: DialogText = DialogText(title),
    useSelectedInSummary: Boolean = false,
    onColorSelected: (Color) -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
){
    val preferenceValue by preference.collectState()

    var showDialog by rememberSaveable { mutableStateOf(false) }

    fun edit(color: Color) {
        try {
            preference.updateValue(color)
            showDialog = false
            onColorSelected(color)
        } catch (e: Exception) {
            Logger.e("ColorPickerPreference", "Could not write preference $preference to database.", e)
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
        ColorPickerDialog(
            dialogText = dialogText,
            onDismiss = { showDialog = false },
            initialColor = preferenceValue,
            onColorSelected = ::edit
        )
    }
}