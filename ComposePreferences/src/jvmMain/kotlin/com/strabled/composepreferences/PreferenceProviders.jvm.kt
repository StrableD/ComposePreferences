package com.strabled.composepreferences

import androidx.compose.runtime.Composable
import com.strabled.composepreferences.utilis.DataStoreManager


/**
 * A Composable function that provides the default DataStoreManager.
 *
 * @return A `DataStoreManager` object representing the default DataStoreManager.
 */
@Composable
actual fun defaultDataStoreManager(): DataStoreManager = DataStoreManager(
    createDataStore {
        System.getProperty("user.home") + "/$defaultDataStoreName$preferenceExtension"
    }
)
