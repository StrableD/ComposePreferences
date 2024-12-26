package com.strabled.composepreferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.CompositionLocal
import androidx.datastore.core.DataStore
import com.strabled.composepreferences.utilis.DataStoreManager

/**
 * Provides a default instance of [DataStoreManager] using the current [LocalContext].
 * The default name for the [DataStore] is "preferences".
 */
@Composable
fun defaultDataStoreManager(): DataStoreManager = DataStoreManager(LocalContext.current)

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
fun setPreferences(preferences: Map<String, Any>) {
    LocalDataStoreManager.current.setPreferences(preferences)
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
fun <T: Any> getPreference(key: String): DataStoreManager.Preference<T> {
    return LocalDataStoreManager.current.getPreference<T>(key)
}