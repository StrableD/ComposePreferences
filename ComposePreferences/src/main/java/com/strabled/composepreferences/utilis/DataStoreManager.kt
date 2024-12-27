package com.strabled.composepreferences.utilis

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * A manager class for handling [DataStore] preferences in a [Android](https://developer.android.com/) application.
 *
 * @param context The application [Context].
 * @param dataStore An optional [DataStore] instance. If not provided, a default instance will be used. The default name is `preferences`.
 */
class DataStoreManager(context: Context, dataStore: DataStore<Preferences>? = null) {

    companion object {
        private val Context.dataStoreManagerPreferenceDataStore by preferencesDataStore(name = "preferences")
    }

    // CoroutineScope for all preferences
    private val scope = CoroutineScope(Dispatchers.IO)

    // DataStore instance
    internal val dataStore = dataStore ?: context.dataStoreManagerPreferenceDataStore

    private val preferences = mutableMapOf<String, Preference<*>>()

    /**
     * Retrieves a [Preference] by its key.
     *
     * @param key The key of the preference.
     * @return The [Preference] associated with the key.
     * @throws IllegalArgumentException if the [Preference] with the given key is not found.
     */
    fun <T : Any> getPreference(key: String): Preference<T> {
        @Suppress("UNCHECKED_CAST")
        return (preferences[key] ?: throw IllegalArgumentException("Preference with key $key not found")) as Preference<T>
    }

    /**
     * Sets multiple [Preference]s at once.
     *
     * @param preferences A map of key-value pairs representing the [Preference]s to be set. The key is the name of the [preference key][Preferences.Key], and the value is the default [Preference] value.
     */
    @Deprecated("Use the setPrefererences(PreferenceBuilder) method instead.", ReplaceWith("setPrefererences(PreferenceBuilder)"), DeprecationLevel.ERROR)
    fun setPreferences(preferences: Map<String, Any>) {
    }

    /**
     * Sets multiple [Preference]s using a [PreferenceBuilder].
     *
     * @param builder The [PreferenceBuilder] containing the preferences to be set.
     *
     * @see PreferenceBuilder
     * @see buildPreferences
     */
    fun setPreferences(builder: PreferenceBuilder) {
        builder.preferences.forEach { (key, value) ->
            this.preferences[key] = value
        }
    }
}

/**
 * Builds preferences using a [PreferenceBuilder] and the provided actions.
 *
 * @param dataStoreManager The [DataStoreManager] instance.
 * @param builderActions The actions to be applied to the [PreferenceBuilder].
 * @return The configured [PreferenceBuilder].
 */
fun buildPreferences(dataStoreManager: DataStoreManager, builderActions: PreferenceBuilder.() -> Unit): PreferenceBuilder {
    val builder = PreferenceBuilder(dataStoreManager)
    builder.builderActions()
    return builder
}