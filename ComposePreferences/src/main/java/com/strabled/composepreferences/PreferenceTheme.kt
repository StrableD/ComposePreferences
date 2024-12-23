package com.strabled.composepreferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Slider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.strabled.composepreferences.preferences.*
import com.strabled.composepreferences.utilis.*

// CompositionLocal for providing the color theme
internal val LocalPreferenceColorTheme = compositionLocalOf { lightPreferenceColorTheme() }

// CompositionLocal for providing the typography
internal val LocalPreferenceTypography = compositionLocalOf { preferenceTypography() }

// CompositionLocal for providing the spacing
internal val LocalPreferenceSpacing = compositionLocalOf { preferenceSpacing() }

/**
 * Contains functions to access the current theme values provided at the call site's position in the hierarchy.
 */
object PreferenceTheme {
    /**
     * Provides the current [PreferenceColorTheme] for preferences.
     * The default theme is [lightPreferenceColorTheme].
     */
    val colorScheme: PreferenceColorTheme
        @Composable @ReadOnlyComposable get() = LocalPreferenceColorTheme.current

    /**
     * Provides the current [PreferenceTypography] for preferences.
     */
    val typography: PreferenceTypography
        @Composable @ReadOnlyComposable get() = LocalPreferenceTypography.current

    /**
     * Provides the current [PreferenceSpacing] for preferences.
     */
    val spacing: PreferenceSpacing
        @Composable @ReadOnlyComposable get() = LocalPreferenceSpacing.current
}

/**
 * The color theme for the preferences.
 * It contains the colors for the [BasicPreference], [PreferenceDialog], [DropDownListPreference], [PreferenceBottomSheet].
 *
 * @property categoryHeaderColor The [Color] of the [CategoryHeader] text in the [PreferenceScreen].
 * @property titleColor The [Color] of the title text in the [BasicPreference].
 * @property summaryColor The [Color] of the summary text in the [BasicPreference].
 * @property leadingIconColor The [Color] of the leading icon in the [BasicPreference].
 * @property trailingContentColor The [Color] of the trailing content in the [BasicPreference].
 * @property backgroundColor The background [Color] of the preferences.
 * @property dividerColor The [Color] of the [HorizontalDivider] in the [PreferenceScreen].
 * @property dialogTitleColor The [Color] of the title text in the [PreferenceDialog].
 * @property dialogContentColor The [Color] of the content text in the [PreferenceDialog].
 * @property dialogBackgroundColor The background [Color] of the [PreferenceDialog].
 * @property dropdownContentColor The [Color] of the content text in the [DropDownListPreference].
 * @property dropdownBackgroundColor The background [Color] of the [DropDownListPreference].
 * @property bottomSheetTitleColor The [Color] of the title text in the [PreferenceBottomSheet].
 * @property bottomSheetContentColor The [Color] of the content text in the [PreferenceBottomSheet].
 * @property bottomSheetBackgroundColor The background [Color] of the [PreferenceBottomSheet].
 * @property bottomSheetDividerColor The [Color] of the [HorizontalDivider] in the [PreferenceBottomSheet].
 */
data class PreferenceColorTheme(
    val categoryHeaderColor: Color,
    val titleColor: Color,
    val summaryColor: Color,
    val leadingIconColor: Color,
    val trailingContentColor: Color,
    val dividerColor: Color,
    val dialogTitleColor: Color,
    val dialogContentColor: Color,
    val dialogBackgroundColor: Color,
    val dropdownContentColor: Color,
    val dropdownBackgroundColor: Color,
    val bottomSheetTitleColor: Color,
    val bottomSheetContentColor: Color,
    val bottomSheetBackgroundColor: Color,
    val bottomSheetDividerColor: Color,
)

// Light color scheme
private val LightColorScheme = lightColorScheme()

// Dark color scheme
private val DarkColorScheme = darkColorScheme()

/**
 * Creates a light [PreferenceColorTheme] for preferences.
 * This is the default theme for preferences.
 *
 * @see PreferenceColorTheme
 */
fun lightPreferenceColorTheme(
    categoryHeaderColor: Color = LightColorScheme.primary,
    titleColor: Color = LightColorScheme.onBackground,
    summaryColor: Color = LightColorScheme.onBackground,
    leadingIconColor: Color = LightColorScheme.primary,
    trailingContentColor: Color = LightColorScheme.onBackground,
    dividerColor: Color = LightColorScheme.outlineVariant,
    dialogTitleColor: Color = LightColorScheme.onSurface,
    dialogContentColor: Color = LightColorScheme.onSurfaceVariant,
    dialogBackgroundColor: Color = LightColorScheme.surfaceContainerHigh,
    dropdownContentColor: Color = LightColorScheme.onSurface,
    dropdownBackgroundColor: Color = LightColorScheme.surfaceContainer,
    bottomSheetTitleColor: Color = LightColorScheme.onSurface,
    bottomSheetContentColor: Color = LightColorScheme.onSurfaceVariant,
    bottomSheetBackgroundColor: Color = LightColorScheme.surfaceContainerLow,
    bottomSheetDividerColor: Color = LightColorScheme.outlineVariant,
) = PreferenceColorTheme(
    categoryHeaderColor = categoryHeaderColor,
    titleColor = titleColor,
    summaryColor = summaryColor,
    leadingIconColor = leadingIconColor,
    trailingContentColor = trailingContentColor,
    dividerColor = dividerColor,
    dialogTitleColor = dialogTitleColor,
    dialogContentColor = dialogContentColor,
    dialogBackgroundColor = dialogBackgroundColor,
    dropdownContentColor = dropdownContentColor,
    dropdownBackgroundColor = dropdownBackgroundColor,
    bottomSheetTitleColor = bottomSheetTitleColor,
    bottomSheetContentColor = bottomSheetContentColor,
    bottomSheetBackgroundColor = bottomSheetBackgroundColor,
    bottomSheetDividerColor = bottomSheetDividerColor,
)

/**
 * Creates a [PreferenceColorTheme] theme for preferences.
 *
 * @see PreferenceColorTheme
 */
fun darkPreferenceColorTheme(
    categoryHeaderColor: Color = DarkColorScheme.primary,
    titleColor: Color = DarkColorScheme.onBackground,
    summaryColor: Color = DarkColorScheme.onBackground,
    leadingIconColor: Color = DarkColorScheme.primary,
    trailingContentColor: Color = DarkColorScheme.onBackground,
    dividerColor: Color = DarkColorScheme.outlineVariant,
    dialogTitleColor: Color = DarkColorScheme.onSurface,
    dialogContentColor: Color = DarkColorScheme.onSurfaceVariant,
    dialogBackgroundColor: Color = DarkColorScheme.surfaceContainerHigh,
    dropdownContentColor: Color = DarkColorScheme.onSurface,
    dropdownBackgroundColor: Color = DarkColorScheme.surfaceContainer,
    bottomSheetTitleColor: Color = DarkColorScheme.onSurface,
    bottomSheetContentColor: Color = DarkColorScheme.onSurfaceVariant,
    bottomSheetBackgroundColor: Color = DarkColorScheme.surfaceContainerLow,
    bottomSheetDividerColor: Color = DarkColorScheme.outlineVariant,
) = PreferenceColorTheme(
    categoryHeaderColor = categoryHeaderColor,
    titleColor = titleColor,
    summaryColor = summaryColor,
    leadingIconColor = leadingIconColor,
    trailingContentColor = trailingContentColor,
    dividerColor = dividerColor,
    dialogTitleColor = dialogTitleColor,
    dialogContentColor = dialogContentColor,
    dialogBackgroundColor = dialogBackgroundColor,
    dropdownContentColor = dropdownContentColor,
    dropdownBackgroundColor = dropdownBackgroundColor,
    bottomSheetTitleColor = bottomSheetTitleColor,
    bottomSheetContentColor = bottomSheetContentColor,
    bottomSheetBackgroundColor = bottomSheetBackgroundColor,
    bottomSheetDividerColor = bottomSheetDividerColor,
)

/**
 * The [Typography] used in the preferences.
 *
 * @property titleStyle The [TextStyle] for the title text in the [BasicPreference].
 * @property summaryStyle The [TextStyle] for the summary text in the [BasicPreference].
 * @property trailingContentStyle The [TextStyle] for the trailing content in the [BasicPreference].
 * @property dialogTitleStyle The [TextStyle] for the title text in the [PreferenceDialog].
 * @property dialogContentStyle The [TextStyle] for the content text in the [PreferenceDialog].
 * @property dropdownContentStyle The [TextStyle] for the content text in the [DropDownListPreference].
 * @property bottomSheetTitleStyle The [TextStyle] for the title text in the [PreferenceBottomSheet].
 * @property bottomSheetContentStyle The [TextStyle] for the content text in the [PreferenceBottomSheet].
 */
data class PreferenceTypography(
    val titleStyle: TextStyle,
    val summaryStyle: TextStyle,
    val trailingContentStyle: TextStyle,
    val dialogTitleStyle: TextStyle,
    val dialogContentStyle: TextStyle,
    val dropdownContentStyle: TextStyle,
    val bottomSheetTitleStyle: TextStyle,
    val bottomSheetContentStyle: TextStyle,
)

// Default typography
private val typography = Typography()

/**
 * Creates the [PreferenceTypography] for preferences using the values defined in [Typography].
 */
fun preferenceTypography(
    titleStyle: TextStyle = typography.titleMedium,
    summaryStyle: TextStyle = typography.bodyMedium,
    trailingContentStyle: TextStyle = typography.bodyMedium,
    dialogTitleStyle: TextStyle = typography.titleLarge.copy(textAlign = TextAlign.Center),
    dialogContentStyle: TextStyle = typography.bodyLarge,
    dropdownContentStyle: TextStyle = typography.bodyMedium,
    bottomSheetTitleStyle: TextStyle = typography.titleLarge.copy(textAlign = TextAlign.Center),
    bottomSheetContentStyle: TextStyle = typography.bodyLarge,
) = PreferenceTypography(
    titleStyle = titleStyle,
    summaryStyle = summaryStyle,
    trailingContentStyle = trailingContentStyle,
    dialogTitleStyle = dialogTitleStyle,
    dialogContentStyle = dialogContentStyle,
    dropdownContentStyle = dropdownContentStyle,
    bottomSheetTitleStyle = bottomSheetTitleStyle,
    bottomSheetContentStyle = bottomSheetContentStyle,
)

/**
 * The spacing used in the preferences.
 *
 * @property preferenceScreenDownset The vertical spacing between the top of the [PreferenceScreen] and the top of the [PreferenceScreen].
 * @property preferenceScreenContentGap The spacing between the content ([PreferenceItem]s and [PreferenceScope.preferenceCategory]s) of the [PreferenceScreen].
 * @property preferenceHorizontalPadding The horizontal padding of the preferences in the [PreferenceScreen] used in the [BasicPreference].
 * @property preferenceVerticalPadding The vertical padding of the preferences in the [PreferenceScreen] used in the [BasicPreference].
 * @property preferenceSingleLineVerticalPadding The vertical padding of the single line preferences in the [PreferenceScreen] used in the [BasicPreference].
 * @property sliderPadding The padding of the [Slider] used in the [SliderPreference].
 * @property categotyTitleIndent The indent of the category title used in the [PreferenceScope.preferenceCategory].
 * @property preferenceTitleIndent The indent of the preference title in the [PreferenceScreen] used in the [TextPreference].
 * @property dividerThickness The thickness of the divider used in the [PreferenceScreen].
 * @property dividerIndent The indent of the divider used in the [PreferenceScreen].
 * @property dialogPadding The padding of the [PreferenceDialog].
 * @property dialogTitlePadding The padding of the [PreferenceDialog] title.
 * @property dialogContentGap The spacing between the content in the [PreferenceDialog].
 * @property dialogContentPadding The padding of the [PreferenceDialog] content.
 * @property dialogContentArrangement The arrangement of the [PreferenceDialog] content.
 * @property dialogButtonPadding The padding of the [PreferenceDialog] buttons.
 * @property dropdownPadding The padding of the [DropdownMenu] used in the [DropDownListPreference].
 * @property dropdownOffset The offset of the [DopdownMenu] used in the [DropDownListPreference].
 * @property dropdownAlignment The alignment of the [DropdownMenu] used in the [DropDownListPreference].
 * @property dropdownContentPadding The padding of the content in the [DropdownMenu] used in the [DropDownListPreference].
 * @property bottomSheetPadding The padding of the [PreferenceBottomSheet].
 * @property bottomSheetTitlePadding The padding of the [PreferenceBottomSheet] title.
 * @property bottomSheetContentGap The spacing between the content in the [PreferenceBottomSheet].
 * @property bottomSheetContentPadding The padding of the [PreferenceBottomSheet] content.
 * @property bottomSheetContentArrangement The arrangement of the [PreferenceBottomSheet] content.
 * @property bottomSheetDividerThickness The thickness of the [HorizontalDivider] used in the [PreferenceBottomSheet] between the title and the content.
 * @property bottomSheetDividerIndent The indent of the [HorizontalDivider] used in the [PreferenceBottomSheet] between the title and the content.
 */
data class PreferenceSpacing(
    val preferenceScreenDownset: Dp,
    val preferenceScreenContentGap: Dp,
    val preferenceHorizontalPadding: Dp,
    val preferenceVerticalPadding: Dp,
    val preferenceSingleLineVerticalPadding: Dp,
    val sliderPadding: Dp,
    val categotyTitleIndent: Dp,
    val preferenceTitleIndent: Dp,
    val dividerThickness: Dp,
    val dividerIndent: Dp,
    val dialogPadding: Dp,
    val dialogTitlePadding: Dp,
    val dialogContentGap: Dp,
    val dialogContentPadding: Dp,
    val dialogContentArrangement: Arrangement.Vertical,
    val dialogButtonPadding: Dp,
    val dropdownPadding: Dp,
    val dropdownOffset: Dp,
    val dropdownAlignment: Alignment.Horizontal,
    val dropdownContentPadding: PaddingValues,
    val bottomSheetPadding: Dp,
    val bottomSheetTitlePadding: Dp,
    val bottomSheetContentGap: Dp,
    val bottomSheetContentPadding: Dp,
    val bottomSheetContentArrangement: Arrangement.Vertical,
    val bottomSheetDividerThickness: Dp,
    val bottomSheetDividerIndent: Dp,
)

/**
 * Creates the [PreferenceSpacing] for preferences.
 */
fun preferenceSpacing(
    preferenceScreenDownset: Dp = 16.dp,
    preferenceScreenContentGap: Dp = 16.dp,
    preferenceHorizontalPadding: Dp = 16.dp,
    preferenceVerticalPadding: Dp = 12.dp,
    preferenceSingleLineVerticalPadding: Dp = 4.dp,
    sliderPadding: Dp = 16.dp,
    categoryTitleIndent: Dp = 16.dp,
    preferenceTitleIndent: Dp = 0.dp,
    dividerThickness: Dp = 1.dp,
    dividerIndent: Dp = 0.dp,
    dialogPadding: Dp = 16.dp,
    dialogTitlePadding: Dp = 0.dp,
    dialogContentGap: Dp = 8.dp,
    dialogContentPadding: Dp = 0.dp,
    dialogContentArrangement: Arrangement.Vertical = Arrangement.spacedBy(dialogContentGap),
    dialogButtonPadding: Dp = 8.dp,
    dropdownPadding: Dp = 4.dp,
    dropdownOffset: Dp = 32.dp,
    dropdownAlignment: Alignment.Horizontal = Alignment.End,
    dropdownContentPadding: PaddingValues = PaddingValues(12.dp, 0.dp),
    bottomSheetPadding: Dp = 16.dp,
    bottomSheetTitlePadding: Dp = 0.dp,
    bottomSheetContentGap: Dp = 16.dp,
    bottomSheetContentPadding: Dp = 0.dp,
    bottomSheetContentArrangement: Arrangement.Vertical = Arrangement.spacedBy(bottomSheetContentGap),
    bottomSheetDividerThickness: Dp = 1.dp,
    bottomSheetDividerIndent: Dp = 0.dp,
) = PreferenceSpacing(
    preferenceScreenDownset = preferenceScreenDownset,
    preferenceScreenContentGap = preferenceScreenContentGap,
    preferenceHorizontalPadding = preferenceHorizontalPadding,
    preferenceVerticalPadding = preferenceVerticalPadding,
    preferenceSingleLineVerticalPadding = preferenceSingleLineVerticalPadding,
    sliderPadding = sliderPadding,
    categotyTitleIndent = categoryTitleIndent,
    preferenceTitleIndent = preferenceTitleIndent,
    dividerThickness = dividerThickness,
    dividerIndent = dividerIndent,
    dialogPadding = dialogPadding,
    dialogTitlePadding = dialogTitlePadding,
    dialogContentGap = dialogContentGap,
    dialogContentPadding = dialogContentPadding,
    dialogContentArrangement = dialogContentArrangement,
    dialogButtonPadding = dialogButtonPadding,
    dropdownPadding = dropdownPadding,
    dropdownOffset = dropdownOffset,
    dropdownAlignment = dropdownAlignment,
    dropdownContentPadding = dropdownContentPadding,
    bottomSheetPadding = bottomSheetPadding,
    bottomSheetTitlePadding = bottomSheetTitlePadding,
    bottomSheetContentGap = bottomSheetContentGap,
    bottomSheetContentPadding = bottomSheetContentPadding,
    bottomSheetContentArrangement = bottomSheetContentArrangement,
    bottomSheetDividerThickness = bottomSheetDividerThickness,
    bottomSheetDividerIndent = bottomSheetDividerIndent,
)