package com.strabled.composepreferences

import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.strabled.composepreferences.utilis.DataStoreManager
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask


/**
 * Creates and returns a default [DataStoreManager] instance configured to store data in the
 * application's documents directory.
 *
 * This function utilizes the iOS file system to locate the application's documents directory and
 * constructs a file path within that directory for storing data store files. The data store file
 * will be named according to `defaultDataStoreName` and will have the `preferenceExtension` extension.
 *
 * The function uses `NSFileManager` to interact with the iOS file system. It retrieves the
 * documents directory URL and appends the default file name to create the complete path.
 *
 * **Important Notes:**
 *
 * - The function is marked with `@OptIn(ExperimentalForeignApi::class)` because it uses APIs
 *   from the `kotlinx-cinterop` library that are considered experimental. This means the API
 *   may change in future releases.
 * - The function uses `requireNotNull` to ensure that the documents directory URL is not null.
 *   If the directory cannot be found, an exception will be thrown.
 * - The data store file is created with a fixed name, defined by `defaultDataStoreName` in combination with `preferenceExtension`.
 *
 * @return A [DataStoreManager] instance configured to manage data in the default data store location.
 * @throws IllegalStateException if the documents directory cannot be found.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun defaultDataStoreManager(): DataStoreManager {
    return DataStoreManager(
        createDataStore {
            val documentsfntDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            )
            requireNotNull(documentsfntDirectory).path + "/$defaultDataStoreName$preferenceExtension"
        }
    )
}

internal fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )