package com.strabled.composepreferencessample

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.strabled.composepreferences.utilis.DialogText
import com.strabled.composepreferences.PreferenceScreen
import com.strabled.composepreferences.ScaffholdComponents
import com.strabled.composepreferences.getPreference
import com.strabled.composepreferences.preferences.*

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun SettingsScreen() {
    val toDisplayString = { value: String ->
        value.mapIndexed { index, char ->
            if (char.isDigit() && index > 0 && !value[index - 1].isDigit()) " $char" else if (index == 0 && char.isLetter()) char.titlecase() else char.toString()
        }.joinToString("")
    }
    PreferenceScreen(scaffoldComponents = ScaffholdComponents(topBar = { SettingsTopBar() })) {
        preferenceCategory("Text Preference") {
            preferenceItem {
                TextPreference(
                    title = "Only some text"
                )
            }
            preferenceItem {
                TextPreference(
                    title = "Only some text",
                    summary = {
                        Text(text = "Now we have a summary")
                    }
                )
            }
            preferenceItem {
                TextPreference(
                    title = "Only some text",
                    summary = {
                        Text(text = "Now it is enabled and clickable")
                    },
                    enabled = true
                )
            }
            preferenceItem {
                TextPreference(
                    title = "Only some text",
                    summary = {
                        Text(text = "Now it is greyed out")
                    },
                    darkenOnDisable = true
                )
            }
            preferenceItem {
                TextPreference(
                    title = "Only some text",
                    summary = {
                        Text(text = "Now with a leading icon")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Info, "Info")
                    }
                )
            }
        }
        preferenceCategory("Switch Preference") {
            preferenceItem {
                SwitchPreference(
                    preference = getPreference("sw1"),
                    title = "Simple switch",
                )
            }
            preferenceItem {
                SwitchPreference(
                    preference = getPreference("sw2"),
                    title = "Simple switch",
                    summary = { Text(text = "Now we have a summary") }
                )
            }
            preferenceItem {
                SwitchPreference(
                    preference = getPreference("sw3"),
                    title = "Simple switch",
                    summary = { Text(text = "But it's disabled") },
                    enabled = false
                )
            }
            preferenceItem {
                SwitchPreference(
                    preference = getPreference("sw4"),
                    title = "Simple switch",
                    summary = { Text(text = "Now with a leading icon") },
                    leadingIcon = { Icon(Icons.Default.Lock, "Lock") }
                )
            }
        }
        preferenceCategory("Slider Preference") {
            preferenceItem {
                SliderPreference(
                    preference = getPreference<Float>("sl1"),
                    title = "Simple slider",
                    showValue = true
                )
            }
            preferenceItem {
                SliderPreference(
                    preference = getPreference<Int>("sl2"),
                    title = "Simple slider",
                    steps = 9,
                    valueRange = 0f..10f,
                    showValue = true
                )
            }
            preferenceItem {
                val preference by getPreference<Double>("sl3")
                val value by preference.flow.collectAsState()
                SliderPreference(
                    preference = getPreference<Double>("sl3"),
                    title = "Simple slider",
                    summary = { Text(text = "Now with a summary containing the value of the slider: %.1f".format(value)) },
                    steps = 49,
                    valueRange = 0f..5f,
                    showValue = false
                )
            }
            preferenceItem {
                SliderPreference(
                    preference = getPreference<Long>("sl4"),
                    title = "Simple slider",
                    summary = { Text(text = "A disabled slider") },
                    enabled = false,
                    showValue = true,
                    valueRange = 0f..10f,
                )
            }
        }
        preferenceCategory("Checkbox Preference") {
            preferenceItem {
                CheckBoxPreference(
                    preference = getPreference<Boolean>("cb1"),
                    title = "Simple checkbox"
                )
            }
            preferenceItem {
                CheckBoxPreference(
                    preference = getPreference<Boolean>("cb2"),
                    title = "Simple checkbox",
                    summary = { Text(text = "Now we have a summary") }
                )
            }
            preferenceItem {
                CheckBoxPreference(
                    preference = getPreference<Boolean>("cb3"),
                    title = "Simple checkbox",
                    summary = { Text(text = "But it's disabled") },
                    enabled = false,
                )
            }
            preferenceItem {
                CheckBoxPreference(
                    preference = getPreference<Boolean>("cb4"),
                    title = "Simple checkbox",
                    summary = { Text(text = "Now with a leading icon") },
                    leadingIcon = { Icon(Icons.Default.Lock, "Lock") }
                )
            }
        }
        preferenceCategory("Edit Text Preference") {
            preferenceItem {
                EditTextPreference(
                    preference = getPreference<String>("et1"),
                    title = "Simple edit text",
                )
            }
            preferenceItem {
                EditTextPreference(
                    preference = getPreference<String>("et2"),
                    title = "Simple edit text",
                    summary = { Text(text = "Now we have a summary") }
                )
            }
            preferenceItem {
                EditTextPreference(
                    preference = getPreference<String>("et3"),
                    title = "Simple edit text",
                    summary = { Text(text = "But it's disabled") },
                    enabled = false,
                    darkenOnDisable = true
                )
            }
            preferenceItem {
                EditTextPreference(
                    preference = getPreference<String>("et4"),
                    title = "Simple edit text",
                    summary = { Text(text = "Now with a leading icon") },
                    leadingIcon = { Icon(Icons.Default.Lock, "Lock") }
                )
            }
            preferenceItem {
                EditTextPreference(
                    preference = getPreference<String>("et6"),
                    title = "Simple edit text",
                    summary = { Text(text = "Now with a custom dialog title and message") },
                    dialogText = DialogText("Custom title", "We have a custom message here")
                )
            }
        }
        preferenceCategory("Select List Preference") {
            preferenceItem {
                DialogListPreference(
                    preference = getPreference<String>("dl1"),
                    title = "Simple select list",
                    items = mapOf("item1" to "Item 1", "item2" to "Item 2"),
                    summary = { Text(text = "Opens a dialog to select from a list of items") }
                )
            }
            preferenceItem {
                DialogListPreference(
                    preference = getPreference<String>("dl2"),
                    title = "Simple select list",
                    items = listOf("item1", "item2", "item3", "item4"),
                    transformToDisplayString = toDisplayString,
                    useSelectedInSummary = true,
                    summary = { Text(text = "We use the selected item [$it] in the summary") }
                )
            }
            preferenceItem {
                DialogListPreference(
                    preference = getPreference<String>("dl2"),
                    title = "Simple select list",
                    items = listOf("item1", "item2", "item3", "item4"),
                    summary = { Text(text = "A disabled select list") },
                    enabled = false,
                    darkenOnDisable = true
                )
            }
        }
        preferenceCategory("Drop Down List Preference") {
            preferenceItem {
                DropDownListPreference(
                    preference = getPreference<String>("dl1"),
                    title = "Simple drop down list",
                    items = mapOf("item1" to "Item 1", "item2" to "Item 2"),
                    summary = { Text(text = "Opens a dialog to select from a list of items") }
                )
            }
            preferenceItem {
                DropDownListPreference(
                    preference = getPreference<String>("dl2"),
                    title = "Simple drop down list",
                    items = listOf("item1", "item2", "item3", "item4"),
                    transformToDisplayString = toDisplayString,
                    useSelectedInSummary = true,
                    summary = { Text(text = "We use the selected item [$it] in the summary") }
                )
            }
            preferenceItem {
                DropDownListPreference(
                    preference = getPreference<String>("dl2"),
                    title = "Simple drop down list",
                    items = listOf("item1", "item2", "item3", "item4"),
                    summary = { Text(text = "A disabled drop down list") },
                    enabled = false,
                    darkenOnDisable = true
                )
            }
        }
        preferenceCategory("BotttomSheet List Preference") {
            preferenceItem {
                BottomSheetListPreference(
                    preference = getPreference<String>("bl1"),
                    title = "Simple bottom sheet list",
                    items = mapOf("item1" to "Item 1", "item2" to "Item 2"),
                    summary = { Text(text = "Opens a bottom sheet to select from a list of items") }
                )
            }
            preferenceItem {
                BottomSheetListPreference(
                    preference = getPreference<String>("bl2"),
                    title = "Simple bottom sheet list",
                    items = listOf("item1", "item2", "item3", "item4"),
                    transformToDisplayString = toDisplayString,
                    useSelectedInSummary = true,
                    summary = { Text(text = "We use the selected item [$it] in the summary") }
                )
            }
            preferenceItem {
                BottomSheetListPreference(
                    preference = getPreference<String>("bl3"),
                    title = "Simple bottom sheet list",
                    items = listOf("item1", "item2", "item3", "item4"),
                    transformToDisplayString = toDisplayString,
                    summary = { Text(text = "A disabled bottom sheet list") },
                    enabled = false,
                    darkenOnDisable = true
                )
            }
            preferenceItem {
                BottomSheetListPreference(
                    preference = getPreference<String>("bl4"),
                    title = "Simple bottom sheet list",
                    items = listOf("item1", "item2", "item3", "item4"),
                    transformToDisplayString = toDisplayString,
                    summary = { Text(text = "A bottom sheet list with a title divider") },
                    showTitleDivider = true
                )
            }
        }
        preferenceCategory("Multi Select List Preference") {
            preferenceItem {
                MultiSelectPreference(
                    preference = getPreference<Set<String>>("msl1"),
                    title = "Simple multi select list",
                    items = mapOf("0" to "Item 1", "10" to "Item 2", "20" to "Item 3", "30" to "Item 4", "40" to "Item 5"),
                    summary = { Text(text = "Opens a dialog for selecting multiple items from a list of items") }
                )
                MultiSelectPreference(
                    preference = getPreference<Set<String>>("msl2"),
                    title = "Simple multi select list",
                    items = listOf("item1", "item2", "item3", "item4", "item5", "item6"),
                    summary = { Text(text = "We use the selected item $it in the summary") },
                    useSelectedInSummary = true,
                    transformToDisplayString = toDisplayString
                )
                MultiSelectPreference(
                    preference = getPreference<Set<String>>("msl3"),
                    title = "Simple multi select list",
                    items = mapOf("0" to "Item 1", "10" to "Item 2", "20" to "Item 3", "30" to "Item 4", "40" to "Item 5"),
                    summary = { Text(text = "A disabled multi select list") },
                    enabled = false,
                    darkenOnDisable = true
                )
            }
        }
        preferenceCategory("Color Picker Preference") {
            preferenceItem {
                ColorPickerPreference<Int>(
                    preference = getPreference("cp1"),
                    title = "Simple color picker",
                    summary = { Text(text = "A color picker dialog") }
                )
            }
            preferenceItem {
                val preferenceData by getPreference<Long>("cp2")
                val value by preferenceData.flow.collectAsState()
                val color = Color(value)
                ColorPickerPreference<Long>(
                    preference = getPreference("cp2"),
                    title = "Simple color picker",
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(color, CircleShape)
                                .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                        )
                    },
                    summary = {
                        Text(
                            text = "Now with a summary containing the selected color [#${
                                color.toArgb().toHexString(HexFormat.UpperCase).substring(2)
                            }] and a leading icon"
                        )
                    }
                )
            }
            preferenceItem {
                ColorPickerPreference<Int>(
                    preference = getPreference("cp3"),
                    title = "Simple color picker",
                    summary = { Text(text = "A disabled color picker") },
                    enabled = false,
                    darkenOnDisable = true
                )
            }
        }
        preferenceFooter(
            title = "Settings Example",
            subtitle = { Text(text = "Version 1.0.0") },
            icon = { Icon(Icons.Outlined.Info, "Settings Footer Info") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge
            )
        }
    )
}