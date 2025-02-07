package com.strabled.composepreferences.utilis

import kotlinx.browser.localStorage
import org.w3c.dom.Storage

/**
 * Manages data storage using a specific storage implementation.
 *
 * This class provides a simplified interface for interacting with a data store,
 * abstracting away the underlying storage mechanism. It extends [BasicDataStoreManager]
 * to inherit core functionality and uses a [Storage] instance for actual data persistence.
 *
 * @see BasicDataStoreManager
 * @see Storage
 */
actual class DataStoreManager : BasicDataStoreManager() {
    internal val store: Storage = localStorage
}