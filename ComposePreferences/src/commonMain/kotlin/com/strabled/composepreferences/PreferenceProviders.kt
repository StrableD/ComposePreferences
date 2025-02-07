package com.strabled.composepreferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.strabled.composepreferences.utilis.DataStoreManager
import com.strabled.composepreferences.utilis.Preference
import com.strabled.composepreferences.utilis.PreferenceBuilder
import com.strabled.composepreferences.utilis.buildPreferences

/**
 * Provides a default instance of [DataStoreManager] using the current [LocalContext].
 * The default name for the [DataStore] is "preferences".
 */
@Composable
expect fun defaultDataStoreManager(): DataStoreManager

internal const val defaultDataStoreName = "preferences"
internal const val preferenceExtension = ".preferences_pb"

/**
 * [CompositionLocal] containing the current [DataStoreManager] that is being used for the preferences.
 * To set the current [DataStoreManager] for the [PreferenceScreen] use [ProvideDataStoreManager].
 */
val LocalDataStoreManager = compositionLocalOf<DataStoreManager> { noLocalProvidedFor("DataStoreManager") }

/**
 * Throws an error indicating that a [CompositionLocal] value is not provided.
 *
 * @param name The name of the [CompositionLocal] value.
 */
internal fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}

/**
 * This function is used to set current value of [LocalDataStoreManager].
 * It uses the [defaultDataStoreManager] to provide the default [DataStoreManager].
 * This function has to be used in order to be able to use [getPreference], [setPreferences] and other functions from [DataStoreManager].
 *
 * @param dataStoreManager The [DataStoreManager] instance to provide. Defaults to [defaultDataStoreManager].
 * @param content The composable content that can access the provided [DataStoreManager].
 */
@Composable
fun ProvideDataStoreManager(dataStoreManager: DataStoreManager = defaultDataStoreManager(), content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalDataStoreManager provides dataStoreManager, content = content)
}

/**
 * A convenience function to set preferences in the current [DataStoreManager].
 * This function can only be used inside of [ProvideDataStoreManager].
 *
 * @param preferences A map of preferences to set.
 * @see ProvideDataStoreManager
 * @see DataStoreManager.setPreferences
 */
@Composable
@Deprecated("Use the setPrefererences(PreferenceBuilder) method instead.", ReplaceWith("setPrefererences(PreferenceBuilder)"), DeprecationLevel.WARNING)
fun setPreferences(preferences: Map<String, Any>) {
    val preferenceBuilder = buildPreferences(LocalDataStoreManager.current) {
        preferences.forEach { (key, value) ->
            key defaultValue value
        }
    }
    setPreferences(builder = preferenceBuilder)
}

/**
 * Sets preferences in the current [DataStoreManager] using a [PreferenceBuilder].
 * This function can only be used inside of [ProvideDataStoreManager].
 *
 * @param builder The [PreferenceBuilder] instance containing the preferences to set.
 * @see ProvideDataStoreManager
 * @see DataStoreManager.setPreferences
 * @see buildPreferences
 */
@Composable
fun setPreferences(builder: PreferenceBuilder) {
    LocalDataStoreManager.current.setPreferences(builder)
}

/**
 * Sets preferences in the current [DataStoreManager] using a lambda with [PreferenceBuilder] receiver.
 * This function can only be used inside of [ProvideDataStoreManager].
 *
 * @param builderActions A lambda with [PreferenceBuilder] receiver to build the preferences.
 * @see ProvideDataStoreManager
 * @see DataStoreManager.setPreferences
 * @see buildPreferences
 */
@Composable
fun setPreferences(builderActions: PreferenceBuilder.() -> Unit) {
    LocalDataStoreManager.current.setPreferences(buildPreferences(LocalDataStoreManager.current, builderActions))
}

/**
 * A convenience function to retrieve a preference from the current [DataStoreManager] by its key.
 * This function can only be used inside of [ProvideDataStoreManager].
 *
 * @param key The key of the preference to retrieve.
 * @return The preference associated with the given key.
 * @see ProvideDataStoreManager
 * @see DataStoreManager.getPreference
 */
@Composable
fun <T : Any> getPreference(key: String): Preference<T> {
    return LocalDataStoreManager.current.getPreference<T>(key)
}