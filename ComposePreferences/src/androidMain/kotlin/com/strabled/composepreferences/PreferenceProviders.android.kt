package com.strabled.composepreferences

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import com.strabled.composepreferences.utilis.DataStoreManager

/**
 * Provides a default instance of [DataStoreManager] using the current [LocalContext].
 * The default name for the [DataStore] is "preferences".
 */
@Composable
actual fun defaultDataStoreManager(): DataStoreManager {
    val context = LocalContext.current
    return DataStoreManager(
        createDataStore {
            context.filesDir.resolve(defaultDataStoreName + preferenceExtension).absolutePath
        }
    )
}
