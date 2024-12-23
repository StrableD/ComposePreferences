package com.strabled.composepreferences.utilis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.strabled.composepreferences.*

/**
 * A [Composable] function that displays a [bottom sheet][ModalBottomSheet] with a [title] and optional [divider][HorizontalDivider].
 *
 * @param title The title text to be displayed at the top of the [bottom sheet][ModalBottomSheet].
 * @param showTitleDivider A boolean flag to show or hide the [divider][HorizontalDivider] below the title.
 * @param onDismiss A lambda function to be called when the [bottom sheet][ModalBottomSheet] is dismissed.
 * @param sheetState The [state][SheetState] of the [bottom sheet][ModalBottomSheet], used to control its visibility and behavior.
 * @param content A composable lambda function that defines the content to be displayed within the [bottom sheet][ModalBottomSheet].
 *
 * Usage:
 * ```
 * PreferenceBottomSheet(
 *     title = "Settings",
 *     showTitleDivider = true,
 *     onDismiss = { /* Handle dismiss */ },
 *     sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
 *     content = {
 *         // Add your composable content here
 *     }
 * )
 * ```
 *
 * For more information, refer to the [ModalBottomSheet](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ModalBottomSheet) documentation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceBottomSheet(
    title: String,
    showTitleDivider: Boolean = false,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PreferenceTheme.colorScheme.bottomSheetBackgroundColor,
        contentColor = PreferenceTheme.colorScheme.bottomSheetContentColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PreferenceTheme.spacing.bottomSheetPadding),
            verticalArrangement = Arrangement.spacedBy(PreferenceTheme.spacing.bottomSheetContentGap)
        ) {
            Text(
                text = title,
                style = PreferenceTheme.typography.bottomSheetTitleStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PreferenceTheme.spacing.bottomSheetTitlePadding),
                color = PreferenceTheme.colorScheme.bottomSheetTitleColor,
            )
            if (showTitleDivider) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = PreferenceTheme.spacing.bottomSheetDividerIndent),
                    color = PreferenceTheme.colorScheme.bottomSheetDividerColor,
                    thickness = PreferenceTheme.spacing.bottomSheetDividerThickness
                )
            }
            ProvideTextStyle(
                PreferenceTheme.typography.bottomSheetContentStyle
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PreferenceTheme.spacing.bottomSheetContentPadding),
                    verticalArrangement = PreferenceTheme.spacing.bottomSheetContentArrangement,
                    content = content
                )
            }
        }
    }
}