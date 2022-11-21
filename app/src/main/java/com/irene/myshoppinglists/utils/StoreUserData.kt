package com.irene.myshoppinglists.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreUserData(private val context: Context) {

    companion object {
        private val Context.dataStoree: DataStore<Preferences> by preferencesDataStore("user")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_ID_KEY = stringPreferencesKey("user_name")
    }

    val getName: Flow<String?> = context.dataStoree.data
        .map { preferences ->
            preferences[USER_NAME_KEY] ?: ""
        }

    suspend fun saveName(name: String) {
        context.dataStoree.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    val getID: Flow<String?> = context.dataStoree.data
        .map { preferences ->
            preferences[USER_ID_KEY] ?: ""
        }

    suspend fun saveID(name: String) {
        context.dataStoree.edit { preferences ->
            preferences[USER_ID_KEY] = name
        }
    }

}