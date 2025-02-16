[![GitHub Release](https://img.shields.io/github/v/release/StrableD/ComposePreferences?include_prereleases&display_name=tag&style=plastic)](https://github.com/StrableD/ComposePreferences/releases/latest)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.strabled/ComposePreferences?style=plastic)](https://central.sonatype.com/artifact/io.github.strabled/ComposePreferences)
[![License](https://img.shields.io/github/license/StrableD/ComposePreferences)](LICENSE.txt)

# Compose Preferences

[Preference](https://developer.android.com/develop/ui/views/components/settings) [Kotlin Multiplatform Project](https://www.jetbrains.com/kotlin-multiplatform/) implementation
for [Jetpack Compose](https://developer.android.com/jetpack/compose) [Material 3](https://developer.android.com/jetpack/compose/designsystems/material3).

This is not an officially supported Google product.

## Preview

<p float="left">
    <img src="/img/img1.png?raw=true" width="150"/>
    <img src="/img/img2.png?raw=true" width="150"/>
    <img src="/img/img3.png?raw=true" width="150"/>
    <img src="/img/img4.png?raw=true" width="150"/>
    <img src="/img/img5.png?raw=true" width="150"/>
</p>
<p float="left">
    <img src="/img/img6.png?raw=true" width="150"/>
    <img src="/img/img7.png?raw=true" width="150"/>
    <img src="/img/img8.png?raw=true" width="150"/>
    <img src="/img/img9.png?raw=true" width="150"/>
    <img src="/img/img10.png?raw=true" width="150"/>
</p>

## Integration

### Gradle (module level):

build.gradle

```gradle
implementation 'io.github.strabled:ComposePreferences:${latest-version}'
```

build.gradle.kts

```kotlin
implementation('io.github.strabled:ComposePreferences:${latest-version}')
//or with verion catalog
implementation(libs.compose.preferences)
```

### Version Catalog

```toml
[versions]
composePreferences = { latest-version }

[libraries]
compose-preferences = { group = "io.github.strabled", name = "ComposePreferences", version.ref = "composePreferences" }
```

## Usage

First you set up your DataStore by using the ``ProvideDataStoreManager`` method outside your Preferencs/Setting screen and set the preferences by using the `PreferenceBuilder`.
The `PreferenceBuilder` can be obtained by using the `buildPreferences` methode. The `PreferenceBuilder` is then passed to the `setPreferences` methode:

```kotlin
ProvideDataStoreManager {
    val preferences = buildPreferences(LocalDataStoreManager.current) {
        "sl1" defaultValue false
        "cp1" defaultValue (Color.Blue serializeWith colorSerializer)
        ..
    }

    setPreferences(preferences)
    SettingsScreen()
}
```

The `buildPreferences` context can also be used directly in the `setPreferences` methode. Or a lambda with a `PreferenceBuilder` receiver can be passed:

```kotlin
val preferences: PreferenceBuilder.() -> Unit = {
    "sl1" defaultValue false
    "cp1" defaultValue (Color.Blue serializeWith colorSerializer)
    ..
}

ProvideDataStoreManager {
    setPreferences(preferences)
    ..
}
```

You can provide your own ``DataStoreManager`` to the ``ProvideDataStoreManager`` if you don't want to use the default one. For Android, this can be done like this:

```kotlin
val Context.dataStore by preferencesDataStore(name = "settings")
val dataStoreManager = DataStoreManager(context = LocalContext.current, dataStore = LocalContext.current.dataStore)

ProvideDataStoreManager(dataStoreManager = dataStoreManager) {
    ..
}
```

Next in your actual Preferences/Settings screen, you can create a ``PreferenceScreen``:

```kotlin
PreferenceScreen() {
    ..
}
```

Here you can define the Scaffhold components such as a Top Bar, Floating Action Button, ... of your ``PreferenceScreen``:

```kotlin
PreferenceScreen(scaffoldComponents = ScaffholdComponents(topBar = { SettingsTopBar() })) {
    ..
}
```

Within the ``PreferenceScreen``, you can add individual preference items using ``preferenceItem``, add a category (group) of items with ``preferenceCategory`` and a footer with
``preferenceFooter``:

```kotlin
preferenceItem { TextPreference(title = "Only some text") }

preferenceCategory("Text Preference") {
    preferenceItem { TextPreference(title = "Only some text") }
    preferenceItem { SliderPreference(preference = getPreference<Float>("sl1"), title = "Simple slider", showValue = true) }
    preferenceItem { CheckBoxPreference(preferences = getPreference<Boolean>("cb1"), title = "Simple checkbox") }
}

preferenceFooter(
    title = "Settings Example",
    subtitle = { Text(text = "Version 1.0.0") },
    icon = { Icon(Icons.Outlined.Info, "Settings Footer Info") }
)
```

If you want to change the appearance of the ``PreferenceScreen`` and the used composables in there, you can give the ``PreferenceScreen`` a custom ``PreferenceColorScheme``,
``PreferenceTypography`` and ``PreferenceSpacing``.

If you only want to change certain values and use the default values for the rest, you can use the ``lightPreferenceColorTheme``, ``darkPreferenceColorTheme``,
``preferenceTypography`` and ``preferenceSpacing``.
This is the preferable approach to not completly destroy the layout of the ``PreferenceScreen`` -- and it's less work for you ;)

```kotlin
val darkTheme = darkPreferenceColorTheme(categoryHeaderColor = MaterialTheme.colorScheme.secondary)
val lightTheme = lightPreferenceColorTheme(categoryHeaderColor = MaterialTheme.colorScheme.secondary)

val typography = preferenceTypography(summaryStyle = MaterialTheme.typography.bodySmall)

val spacing = preferenceSpacing(dialogContentGap = 16.dp)

PreferenceScreen(
    theme = if (isSystemInDarkTheme()) darkTheme else lightTheme,
    typography = typography,
    spacing = spacing
) {
    ..
}
```

If you want to use the `Preferences` outside a fullsize componenet, you can use the `PreferenceContainer`:

```kotlin
PreferenceContainer {
    ..
}
```

To get the preference, you can use the convinience function `getPreference` inside `ProvideDataStoreManager` which retrieves the preference from the `DataStoreManager` by the given
key.
A `StateFlow` of the value of the preference can be obtained by delegating the preference to a variable, and it can be set with the `Preference.set` method.

```kotlin
val preference: Preference<Int> = getPreference<Int>("intKey")
val preferenceData: StateFlow<Int> by preference
val preferenceValue: Int by preferenceData.collectAsState()
```

The preferences support serialization with the [kotlin serialization plugin](https://kotlinlang.org/docs/serialization.html).

```kotlin
@Serializable
data class User(val name: String, val age: Int)

val preferenceData: StateFlow<User> by getPreference("userKey")
```

## Preferences

Each preference composable excluding TextPref has a mandatory ``preference`` parameter. This ``preference`` has the ``key name`` that will be used in the DataStore and is
equivalent to the ``key`` used in previous Android preference libraries.

You should be using unique keys for each preference. Using the same key for different preferences of the same type will result in their values being the same. Using the same key
for different preferences of different types may result in unexpected behavior.

### [TextPreference](ComposePreferences/src/main/java/com/strabled/composepreferences/preferences/TextPreference.kt)

```kotlin
TextPreference(
    title = "Only some text",
    summary = {
        Text(text = "Now we have a summary")
    }
)
```

### [SwitchPreference](ComposePreferences/src/main/java/com/strabled/composepreferences/preferences/SwitchPreference.kt)

```kotlin
SwitchPreference(
    preference = getPreference("sw4"),
    title = "Simple switch",
    summary = { Text(text = "Now with a leading icon") },
    leadingIcon = { Icon(Icons.Default.Lock, "Lock") }
)
```

### [SliderPreference](ComposePreferences/src/main/java/com/strabled/composepreferences/preferences/SliderPreference.kt)

```kotlin
SliderPreference(
    preference = getPreference<Int>("sl2"),
    title = "Simple slider",
    steps = 9,
    valueRange = 0f..10f,
    showValue = true
)
```

### [CheckBoxPreference](ComposePreferences/src/main/java/com/strabled/composepreferences/preferences/CheckBoxPreference.kt)

```kotlin
CheckBoxPreference(
    preference = getPreference<Boolean>("cb4"),
    title = "Simple checkbox",
    summary = { Text(text = "Now with a leading icon") },
    leadingIcon = { Icon(Icons.Default.Lock, "Lock") }
)
```

### [EditTextPreference](ComposePreferences/src/main/java/com/strabled/composepreferences/preferences/EditTextPreference.kt)

```kotlin
EditTextPreference(
    preference = getPreference<String>("et6"),
    title = "Simple edit text",
    summary = { Text(text = "Now with a custom dialog title and message") },
    dialogText = DialogText("Custom title", "We have a custom message here")
)
```

### [DialogListPreference](ComposePreferences/src/main/java/com/strabled/composepreferences/preferences/DialogListPreference.kt)

```kotlin
DialogListPreference(
    preference = getPreference<String>("dl1"),
    title = "Simple select list",
    items = mapOf("item1" to "Item 1", "item2" to "Item 2"),
    summary = { Text(text = "Opens a dialog to select from a list of items") }
)
```

### [DropDownListPreference](ComposePreferences/src/main/java/com/strabled/composepreferences/preferences/DropDownListPreference.kt)

```kotlin
DropDownListPreference(
    preference = getPreference<String>("dl1"),
    title = "Simple drop down list",
    items = mapOf("item1" to "Item 1", "item2" to "Item 2"),
    summary = { Text(text = "Opens a dialog to select from a list of items") }
)
```

### [BottomSheetListPreference](ComposePreferences/src/main/java/com/strabled/composepreferences/preferences/BottomSheetListPreference.kt)

```kotlin
BottomSheetListPreference(
    preference = getPreference<String>("bl1"),
    title = "Simple bottom sheet list",
    items = mapOf("item1" to "Item 1", "item2" to "Item 2"),
    summary = { Text(text = "Opens a bottom sheet to select from a list of items") }
)
```

### [MultiSelectPreference](ComposePreferences/src/main/java/com/strabled/composepreferences/preferences/MultiSelectPreference.kt)

```kotlin
MultiSelectPreference(
    preference = getPreference<Set<String>>("msl1"),
    title = "Simple multi select list",
    items = mapOf("0" to "Item 1", "10" to "Item 2", "20" to "Item 3", "30" to "Item 4", "40" to "Item 5"),
    summary = { Text(text = "Opens a dialog for selecting multiple items from a list of items") }
)
```

### [ColorPickerPreference](ComposePreferences/src/main/java/com/strabled/composepreferences/preferences/ColorPickerPreference.kt)

```kotlin
ColorPickerPreference(
    preference = getPreference("cp1"),
    title = "Simple color picker",
    summary = { Text(text = "A color picker dialog") }
)
```

And that's it! You can create your whole preference screen in this way, and you can modify the individual parameters of each preference composable to achieve the functionality you
require.
If something is missing, please create an Issue so we can discuss possible solutions.

## Contributing

You can help this project by contributing to it. 
If you have a feature request or have found a bug, feel free to share this by following the [CONTRIBUTING.md](CONTRIBUTING.md) guidelines.
Any help is appreciated!

## License

[License](LICENSE.txt)
