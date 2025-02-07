package com.strabled.composepreferences.utilis

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * `DataStoreManager` is a concrete implementation of [BasicDataStoreManager]
 * that utilizes Android's DataStore with Preferences to manage key-value pairs persistently.
 *
 * It provides methods for reading, writing, and clearing data within the DataStore.
 *
 * This class is designed to be used in actual Android applications where DataStore is available.
 *
 * @property dataStore The DataStore instance responsible for managing preferences.
 *                     It's injected into this class and should be pre-configured
 *                     (e.g., using `DataStoreFactory.create`) before being passed in.
 * @constructor Creates a `DataStoreManager` instance with the provided DataStore.
 */
actual class DataStoreManager(internal val dataStore: DataStore<Preferences>) : BasicDataStoreManager()