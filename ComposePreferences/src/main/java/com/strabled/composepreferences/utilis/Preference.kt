package com.strabled.composepreferences.utilis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.KProperty

/**
 * Interface representing a preference in the [DataStoreManager].
 *
 * @param T The type of the preference value.
 */
interface Preference<T> {
    /**
     * Gets the value of the preference as a [StateFlow].
     * This function is used by the [delegation mechanism](https://kotlinlang.org/docs/delegated-properties.html) of Kotlin.
     *
     * @param thisRef The reference to the object containing the property.
     * @param property The metadata for the property.
     * @return The current value of the preference as a [StateFlow].
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<T>

    /**
     * Collects the state of the preference as a [State].
     *
     * @return The collected [State] of the preference.
     */
    @Composable
    fun collectState(): State<T>

    /**
     * Updates the value of the preference.
     *
     * @param value The new value to set.
     */
    fun updateValue(value: T)
}

/**
 * Implementation of [Preference] for primitive types.
 *
 * @param T The type of the preference value.
 * @param dataStore The DataStore instance to use for storing preferences.
 * @param keyName The name of the preference key.
 * @param defaultValue The default value of the preference.
 */
internal class PrimitivePreference<T>(dataStore: DataStore<Preferences>, keyName: String, private val defaultValue: T) : Preference<T> {
    @Suppress("UNCHECKED_CAST")
    private val key: Preferences.Key<T> = when (defaultValue) {
        is Boolean -> booleanPreferencesKey(keyName)
        is Int -> intPreferencesKey(keyName)
        is Long -> longPreferencesKey(keyName)
        is Float -> floatPreferencesKey(keyName)
        is String -> stringPreferencesKey(keyName)
        else -> throw IllegalArgumentException("Unsupported preference type")
    } as Preferences.Key<T>
    private val data: PreferenceData<T> = PrimitivePreferenceData(dataStore, key, defaultValue)

    override fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<T> = data.flow

    @Composable
    override fun collectState(): State<T> = data.flow.collectAsState(initial = defaultValue)

    override fun updateValue(value: T) = data.setValue(value)
}

/**
 * Implementation of [Preference] for sets of strings.
 *
 * @param dataStore The DataStore instance to use for storing preferences.
 * @param keyName The name of the preference key.
 * @param defaultValue The default value of the preference.
 */
internal class StringSetPreference(dataStore: DataStore<Preferences>, keyName: String, private val defaultValue: Set<String>) : Preference<Set<String>> {
    private val key: Preferences.Key<Set<String>> = stringSetPreferencesKey(keyName)
    private val data: PreferenceData<Set<String>> = PrimitivePreferenceData(dataStore, key, defaultValue)

    override fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<Set<String>> = data.flow

    @Composable
    override fun collectState(): State<Set<String>> = data.flow.collectAsState(initial = defaultValue)

    override fun updateValue(value: Set<String>) = data.setValue(value)
}

/**
 * Implementation of [Preference] for serializable types.
 *
 * @param T The type of the preference value.
 * @param dataStore The DataStore instance to use for storing preferences.
 * @param keyName The name of the preference key.
 * @param defaultValue The default value of the preference.
 * @param serializer The serializer for the preference value.
 */
internal class SerializablePreference<T>(
    dataStore: DataStore<Preferences>,
    keyName: String,
    private val defaultValue: T,
    serializer: KSerializer<T>
) : Preference<T> {
    private val key: Preferences.Key<String> = stringPreferencesKey(keyName)
    private var data: PreferenceData<T> = SerializablePreferenceData(dataStore, key, defaultValue, serializer)

    override fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<T> = data.flow

    @Composable
    override fun collectState(): State<T> = data.flow.collectAsState(initial = defaultValue)

    override fun updateValue(value: T) = data.setValue(value)
}

/**
 * Abstract class representing preference data.
 *
 * @param T The type of the preference value.
 */
internal abstract class PreferenceData<T> {
    internal abstract val scope: CoroutineScope
    internal abstract fun setValue(value: T)
    internal abstract val flow: StateFlow<T>
}

/**
 * Implementation of [PreferenceData] for primitive types.
 *
 * @param T The type of the preference value.
 * @param dataStore The DataStore instance to use for storing preferences.
 * @param key The key for the preference.
 * @param defaultValue The default value of the preference.
 */
internal class PrimitivePreferenceData<T>(private val dataStore: DataStore<Preferences>, private val key: Preferences.Key<T>, private val defaultValue: T) : PreferenceData<T>() {
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override val flow: StateFlow<T> = dataStore.data
        .map { preferences ->
            preferences[key] ?: defaultValue
        }
        .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = defaultValue)

    override fun setValue(value: T) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences[key] = value
            }
        }
    }
}

/**
 * Implementation of [PreferenceData] for serializable types.
 *
 * @param T The type of the preference value.
 * @param dataStore The DataStore instance to use for storing preferences.
 * @param key The key for the preference.
 * @param defaultValue The default value of the preference.
 * @param serializer The serializer for the preference value.
 */
internal class SerializablePreferenceData<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<String>,
    private val defaultValue: T,
    private val serializer: KSerializer<T>
) : PreferenceData<T>() {
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override val flow: StateFlow<T> = dataStore.data
        .map { preferences ->
            preferences[key]?.let { Json.decodeFromString(serializer, it) } ?: defaultValue
        }
        .stateIn(scope = scope, started = SharingStarted.Companion.Eagerly, initialValue = defaultValue)

    override fun setValue(value: T) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences[key] = Json.encodeToString(serializer, value)
            }
        }
    }
}

/**
 * Builder class for creating [Preference]s.
 *
 * @param dataStoreManager The manager for the [DataStore] instance.
 * @see buildPreferences
 */
class PreferenceBuilder internal constructor(dataStoreManager: DataStoreManager) {
    private val dataStore = dataStoreManager.dataStore
    internal val preferences: MutableMap<String, Preference<out Any>> = mutableMapOf<String, Preference<out Any>>()

    /**
     * Adds a [Preference] by using the [defaultValue].
     *
     * @param defaultValue The default value of the [Preference].
     */
    @OptIn(InternalSerializationApi::class)
    infix fun <T : Any> String.defaultValue(defaultValue: T) {
        if (defaultValue::class.serializerOrNull() == null) {
            if (defaultValue is Set<*>) {
                @Suppress("UNCHECKED_CAST")
                preferences[this] = StringSetPreference(dataStore = dataStore, keyName = this, defaultValue = defaultValue as Set<String>)
            } else {
                preferences[this] = PrimitivePreference(dataStore = dataStore, keyName = this, defaultValue = defaultValue)
            }
        } else {
            preferences[this] =
                SerializablePreference(dataStore = dataStore, keyName = this, defaultValue = defaultValue, serializer = defaultValue::class.serializer() as KSerializer<T>)
        }
    }

    /**
     * Adds a serializable [Preference] by using the [defaultValue] and a given [serializer].
     * This function can be used to add a type which has no [kotlinx.serialization.Serializable] annotation and cannot be used in the [DataStore].
     * With the provided [serializer] the type can be used in the [DataStore].
     *
     * To provide a custom serializer the [KSerializer] has to be implemented.
     * To provide the serializer the [serializeWith] function can be used.
     * ```kotlin
     *  val serializer = object : KSerializer<MyType> {
     *      override val descriptor: SerialDescriptor = ..
     *      override fun serialize(encoder: Encoder, value: MyType) {
     *          ..
     *      }
     *      override fun deserialize(decoder: Decoder): MyType {
     *          ..
     *      }
     *  }
     *
     *  val preference = PreferenceBuilder.() -> Unit = {
     *      "myKey" defaultValue (MyType() serializeWith serializer)
     *  }
     * ```
     *
     * @see serializeWith
     *
     * @param value A pair of the default value and the serializer.
     */
    infix fun <T : Any> String.defaultValue(value: Pair<T, KSerializer<T>>) {
        val (defaultValue, serializer) = value
        preferences[this] = SerializablePreference(dataStore = dataStore, keyName = this, defaultValue = defaultValue, serializer = serializer)
    }

    /**
     * Creates a [Pair] of a [value][T] and a [serializer].
     * This function is used in combination with [defaultValue] to provide a custom serializer for a type which has no [kotlinx.serialization.Serializable] annotation and can therefore not be used in the [DataStore].
     *
     * @param serializer The serializer for the value.
     * @return A [Pair] of the value and the [serializer][KSerializer].
     * @see defaultValue
     */
    infix fun <T : Any> T.serializeWith(serializer: KSerializer<T>): Pair<T, KSerializer<T>> = this to serializer
}