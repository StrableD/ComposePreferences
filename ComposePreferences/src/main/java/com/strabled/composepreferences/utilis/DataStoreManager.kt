package com.strabled.composepreferences.utilis

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KProperty

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
    private val dataStore = dataStore ?: context.dataStoreManagerPreferenceDataStore

    private val preferences = mutableMapOf<String, Preference<*>>()

    /**
     * Retrieves a [Preference] by its key.
     *
     * @param key The key of the preference.
     * @return The [Preference] associated with the key.
     * @throws IllegalArgumentException if the [Preference] with the given key is not found.
     */
    fun <T> getPreference(key: String): Preference<T>{
        @Suppress("UNCHECKED_CAST")
        return (preferences[key] ?: throw IllegalArgumentException("Preference with key $key not found")) as Preference<T>
    }

    /**
     * Sets multiple [Preference]s at once.
     *
     * @param preferences A map of key-value pairs representing the [Preference]s to be set. The key is the name of the [preference key][Preferences.Key], and the value is the default [Preference] value.
     */
    fun setPreferences(preferences: Map<String, *>) {
        preferences.forEach { (key, value) ->
            this.preferences[key] = Preference(key, value)
        }
    }

    /**
     * A class representing a single [Preference].
     *
     * @param keyName The name of the [preference key][Preferences.Key].
     * @param defaultValue The default value of the [Preference].
     */
    inner class Preference<T>(
        private val keyName: String,
        private val defaultValue: T,
    ) {

        @Suppress("UNCHECKED_CAST")
        private val key: Preferences.Key<T> = when (defaultValue) {
            is String -> stringPreferencesKey(keyName)
            is Int -> intPreferencesKey(keyName)
            is Boolean -> booleanPreferencesKey(keyName)
            is Float -> floatPreferencesKey(keyName)
            is Long -> longPreferencesKey(keyName)
            is Double -> floatPreferencesKey(keyName)
            else -> {
                if (defaultValue.isStringSet()) {
                    stringSetPreferencesKey(keyName)
                } else if (defaultValue.isStringConvertible()) {
                    stringPreferencesKey(keyName)
                } else {
                    throw IllegalArgumentException("Unsupported type ${defaultValue?.let { it::class } ?: defaultValue}")
                }
            }
        } as Preferences.Key<T>

        private val preferenceData = PreferenceData(
            key = key,
            defaultValue = defaultValue,
        )

        operator fun getValue(thisRef: Any?, property: KProperty<*>): PreferenceData<T> {
            return preferenceData
        }
    }

    /**
     * A class representing the data of a [Preference].
     *
     * @param dataStore The [DataStore] instance.
     * @param key The [key][Preferences.Key] of the [Preference].
     * @param defaultValue The default value of the [Preference].
     * @param scope The [CoroutineScope] for managing coroutines.
     */
    inner class PreferenceData<T>(
        private val key: Preferences.Key<T>,
        private val defaultValue: T,
    ) {
        @OptIn(InternalSerializationApi::class)
        val flow: StateFlow<T> = dataStore.data
            .map { preferences ->
                if (defaultValue.isStringConvertible()) {
                    (preferences[key] as String?)?.let { Json.decodeFromString(defaultValue!!::class.serializer(), it) } ?: defaultValue
                } else {
                    preferences[key] ?: defaultValue
                }
            }
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = defaultValue
            )

        /**
         * Sets a new value for the [Preference].
         *
         * @param newValue The new value to be set.
         */
        fun set(newValue: T) {
            scope.launch {
                dataStore.edit { preferences ->
                    preferences[key] = newValue
                }
            }
        }
    }
}

/**
 * Extension function to check if a value is a [Set] of [String]s.
 *
 * @return True if the value is a [Set] of [String]s, false otherwise.
 */
private fun <T> T.isStringSet(): Boolean {
    return this is Set<*> && this.all { it is String }
}

/**
 * Extension function to check if a value can be converted to a [String].
 *
 * @return True if the value can be converted to a [String], false otherwise.
 */
private fun <T> T.isStringConvertible(): Boolean {
    return if (this == null) false else {
        try {
            val string = Json.encodeToString<Any>(this)
            Json.decodeFromString<Any>(string)
            true
        } catch (e: Exception) {
            false
        }
    }
}