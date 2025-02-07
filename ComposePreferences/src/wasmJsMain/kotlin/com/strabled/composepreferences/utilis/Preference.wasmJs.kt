package com.strabled.composepreferences.utilis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull
import org.w3c.dom.Storage
import kotlin.reflect.KProperty

/**
 * Implementation of [Preference] for primitive types.
 *
 * @param T The type of the preference value.
 * @param store The [Storage] instance to use for storing preferences.
 * @param keyName The name of the preference key.
 * @param defaultValue The default value of the preference.
 */
internal actual class PrimitivePreference<T>(store: Storage, keyName: String, private val defaultValue: T) : Preference<T> {

    private val data: PreferenceData<T> = PrimitivePreferenceData(store, keyName, defaultValue)

    actual override fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<T> = data.flow

    @Composable
    actual override fun collectState(): State<T> = data.flow.collectAsState(initial = defaultValue)

    actual override fun updateValue(value: T) = data.setValue(value)
}

/**
 * Implementation of [Preference] for sets of strings.
 *
 * @param store The [Storage] instance to use for storing preferences.
 * @param keyName The name of the preference key.
 * @param defaultValue The default value of the preference.
 */
internal actual class StringSetPreference(store: Storage, keyName: String, private val defaultValue: Set<String>) : Preference<Set<String>> {

    private val data: PreferenceData<Set<String>> = PrimitivePreferenceData(store, keyName, defaultValue)

    actual override fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<Set<String>> = data.flow

    @Composable
    actual override fun collectState(): State<Set<String>> = data.flow.collectAsState(initial = defaultValue)

    actual override fun updateValue(value: Set<String>) = data.setValue(value)
}

/**
 * Implementation of [Preference] for serializable types.
 *
 * @param T The type of the preference value.
 * @param store The [Storage] instance to use for storing preferences.
 * @param keyName The name of the preference key.
 * @param defaultValue The default value of the preference.
 * @param serializer The serializer for the preference value.
 */
internal actual class SerializablePreference<T>(
    store: Storage,
    keyName: String,
    private val defaultValue: T,
    serializer: KSerializer<T>
) : Preference<T> {

    private val data: PreferenceData<T> = SerializablePreferenceData(store, keyName, defaultValue, serializer)

    actual override fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<T> = data.flow

    @Composable
    actual override fun collectState(): State<T> = data.flow.collectAsState(initial = defaultValue)

    actual override fun updateValue(value: T) = data.setValue(value)
}

/**
 * Implementation of [PreferenceData] for primitive types.
 *
 * @param T The type of the preference value.
 * @param store The [Storage] instance to use for storing preferences.
 * @param keyName The name of the key for the preference.
 * @param defaultValue The default value of the preference.
 */
internal actual class PrimitivePreferenceData<T>(private val store: Storage, private val keyName: String, private val defaultValue: T) : PreferenceData<T>() {
    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    private val serializer: KSerializer<T> = when (defaultValue) {
        is Boolean -> Boolean::class.serializer()
        is Int -> Int::class.serializer()
        is Long -> Long::class.serializer()
        is Float -> Float::class.serializer()
        is Double -> Double::class.serializer()
        is String -> String::class.serializer()
        else -> throw IllegalArgumentException("Unsupported preference type")
    } as KSerializer<T>

    actual override val scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined)
    actual override val flow: StateFlow<T> = MutableStateFlow(
        store.getItem(keyName)?.let { Json.decodeFromString(serializer, it) } ?: defaultValue
    )

    actual override fun setValue(value: T) {
        scope.launch {
            store.setItem(keyName, Json.encodeToString(serializer, value))
        }
    }
}

/**
 * Implementation of [PreferenceData] for serializable types.
 *
 * @param T The type of the preference value.
 * @param store The [Storage] instance to use for storing preferences.
 * @param keyName The name of the key for the preference.
 * @param defaultValue The default value of the preference.
 * @param serializer The serializer for the preference value.
 */
internal actual class SerializablePreferenceData<T>(
    private val store: Storage,
    private val keyName: String,
    private val defaultValue: T,
    private val serializer: KSerializer<T>
) : PreferenceData<T>() {
    actual override val scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined)

    actual override val flow: StateFlow<T> = MutableStateFlow(
        store.getItem(keyName)?.let { Json.decodeFromString(serializer, it) } ?: defaultValue
    )

    actual override fun setValue(value: T) {
        scope.launch {
            store.setItem(keyName, Json.encodeToString(serializer, value))
        }
    }
}

/**
 * Builder class for creating and configuring [Preference] instances.
 *
 * This class provides a fluent interface for defining preferences with default values and custom serializers.
 * It handles the underlying storage mechanism and ensures proper serialization/deserialization.
 *
 * @param dataStoreManager The [DataStoreManager] responsible for managing the underlying data store.
 * @see buildPreferences
 */
actual class PreferenceBuilder internal actual constructor(dataStoreManager: DataStoreManager) {
    private val store: Storage = dataStoreManager.store
    internal actual val preferences: MutableMap<String, Preference<out Any>> = mutableMapOf()

    /**
     * Adds a [Preference] by using the [defaultValue].
     *
     * @param defaultValue The default value of the [Preference].
     */
    @OptIn(InternalSerializationApi::class)
    actual infix fun <T : Any> String.defaultValue(defaultValue: T) {
        if (defaultValue::class.serializerOrNull() == null) {
            if (defaultValue is Set<*>) {
                @Suppress("UNCHECKED_CAST")
                preferences[this] = StringSetPreference(store = store, keyName = this, defaultValue = defaultValue as Set<String>)
            } else {
                preferences[this] = PrimitivePreference(store = store, keyName = this, defaultValue = defaultValue)
            }
        } else {
            @Suppress("UNCHECKED_CAST")
            preferences[this] = SerializablePreference(store = store, keyName = this, defaultValue = defaultValue, serializer = defaultValue::class.serializer() as KSerializer<T>)
        }
    }

    /**
     * Adds a serializable [Preference] by using the [defaultValue] and a given [serializer].
     * This function can be used to add a type which has no [kotlinx.serialization.Serializable] annotation and cannot be used in the [Storage].
     * With the provided [serializer] the type can be used in the [Storage].
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
    actual infix fun <T : Any> String.defaultValue(value: Pair<T, KSerializer<T>>) {
        val (defaultValue, serializer) = value
        preferences[this] = SerializablePreference(store = store, keyName = this, defaultValue = defaultValue, serializer = serializer)
    }

    /**
     * Creates a [Pair] of a [value][T] and a [serializer].
     * This function is used in combination with [defaultValue] to provide a custom serializer for a type which has no [kotlinx.serialization.Serializable] annotation and can therefore not be used in the [Storage].
     *
     * @param serializer The serializer for the value.
     * @return A [Pair] of the value and the [serializer][KSerializer].
     * @see defaultValue
     */
    actual infix fun <T : Any> T.serializeWith(serializer: KSerializer<T>): Pair<T, KSerializer<T>> = this to serializer
}