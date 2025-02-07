package com.strabled.composepreferences.utilis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
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
 */
internal expect class PrimitivePreference<T> : Preference<T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<T>

    @Composable
    override fun collectState(): State<T>

    override fun updateValue(value: T)
}

/**
 * Implementation of [Preference] for sets of strings.
 */
internal expect class StringSetPreference : Preference<Set<String>> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<Set<String>>

    @Composable
    override fun collectState(): State<Set<String>>

    override fun updateValue(value: Set<String>)
}

/**
 * Implementation of [Preference] for serializable types.
 *
 * @param T The type of the preference value.
 */
internal expect class SerializablePreference<T> : Preference<T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<T>

    @Composable
    override fun collectState(): State<T>

    override fun updateValue(value: T)
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
 */
internal expect class PrimitivePreferenceData<T> : PreferenceData<T> {
    override val scope: CoroutineScope

    override val flow: StateFlow<T>

    override fun setValue(value: T)
}

/**
 * Implementation of [PreferenceData] for serializable types.
 *
 * @param T The type of the preference value.
 */
internal expect class SerializablePreferenceData<T> : PreferenceData<T> {
    override val scope: CoroutineScope

    override val flow: StateFlow<T>

    override fun setValue(value: T)
}

/**
 * Builder class for creating and managing [Preference] instances.
 * This class provides a fluent API for defining preferences with default values and optional custom serializers.
 *
 * @property preferences A mutable map holding the defined preferences, keyed by their string name.
 * @constructor Creates a new instance of [PreferenceBuilder].
 * @param dataStoreManager The [DataStoreManager] instance associated with the preferences.
 */
expect class PreferenceBuilder internal constructor(dataStoreManager: DataStoreManager) {
    internal val preferences: MutableMap<String, Preference<out Any>>

    /**
     * Adds a [Preference] by using the [defaultValue].
     *
     * @param defaultValue The default value of the [Preference].
     */
    infix fun <T : Any> String.defaultValue(defaultValue: T)

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
    infix fun <T : Any> String.defaultValue(value: Pair<T, KSerializer<T>>)

    /**
     * Creates a [Pair] of a [value][T] and a [serializer].
     * This function is used in combination with [defaultValue] to provide a custom serializer for a type which has no [kotlinx.serialization.Serializable] annotation and can therefore not be used in the [DataStore].
     *
     * @param serializer The serializer for the value.
     * @return A [Pair] of the value and the [serializer][KSerializer].
     * @see defaultValue
     */
    infix fun <T : Any> T.serializeWith(serializer: KSerializer<T>): Pair<T, KSerializer<T>>
}