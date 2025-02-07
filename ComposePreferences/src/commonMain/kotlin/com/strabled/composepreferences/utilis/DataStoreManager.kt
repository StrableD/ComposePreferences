package com.strabled.composepreferences.utilis

/**
 * A manager class for handling persistent data storage in a Kotlin Multiplatform (KMP) project.
 *
 * This class provides a simplified, platform-agnostic interface for storing and retrieving key-value pairs across different platforms (e.g., Android, iOS).
 * It abstracts away the platform-specific details of how data is persisted, allowing you to write common code that works seamlessly on all supported platforms.
 *
 * This `expect` declaration defines the common interface for persistent data storage functionality.
 * Platform-specific `actual` implementations will provide the concrete behavior, using the appropriate storage mechanisms for each platform.
 *
 * The common code should primarily interact with this `DataStoreManager` and will be completely platform-agnostic.
 *
 * To use the `DataStoreManager`, you must implement it in each target platform (Android, iOS, etc.).
 * The `actual` implementations will handle the platform-specific logic of data storage and retrieval.
 *
 * @constructor Creates a new [DataStoreManager] instance.
 * The exact initialization process may vary depending on the platform.
 *
 * Platform-specific notes (implementation details):
 * - **Android: **Typically uses the Preferences DataStore or SharedPreferences, but it's up to you to implement. You will likely need to inject the [android.content.Context] to create the instance.
 * - **iOS: ** Usually utilizes `NSUserDefaults`, `Keychain`, or a similar mechanism. Check the `actual` implementation for specific details.
 * - **JVM: **You can use a file or any custom implementation for data storage.
 * - **Native: **It's up to you to create a way to store data, a file, a local database ...
 *
 * Example Usage (in common code):
 *
 * ```kotlin
 * // Note: The instance must be provided by the platform-specific code.
 * // For example,
 * // - Android: val dataStoreManager = DataStoreManager(applicationContext)
 * // - iOS: val dataStoreManager = DataStoreManager() // Or a platform-specific init.
 * // - JVM: val dataStoreManager = DataStoreManager() // Or a platform-specific init.
 * // - Native: val dataStoreManager = DataStoreManager() // Or a platform-specific init. */
expect class DataStoreManager : BasicDataStoreManager

abstract class BasicDataStoreManager {

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
     * @param preferences A map of key-value pairs representing the [Preference]s to be set. The key is the name of the preference key, and the value is the default [Preference] value.
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