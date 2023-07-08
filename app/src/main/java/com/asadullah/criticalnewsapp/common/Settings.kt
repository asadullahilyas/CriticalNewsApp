package com.asadullah.criticalnewsapp.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class Settings @Inject constructor(
    context: Context,
) {

    private val dataStore: DataStore<Preferences>

    init {
        dataStore = PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("settings")
        }
    }

    private val keyIsBiometricAuthEnabled = booleanPreferencesKey("IS_BIOMETRIC_AUTH_ENABLED")

    fun getBiometricAuthEnabled(): Flow<Boolean> {
        return dataStore.data.map { settings ->
            settings[keyIsBiometricAuthEnabled] ?: false
        }
    }

    suspend fun setBiometricAuthEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[keyIsBiometricAuthEnabled] = enabled
        }
    }
}