package com.aplikasi.capstone
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[ID_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PROFILE_KEY] ?: "",
                preferences[USERNAME_KEY]   ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }



    suspend fun clearUser() {
        dataStore.edit {
            it.clear()
        }
    }


    suspend fun saveUser(account: UserModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = account.id
            preferences[USERNAME_KEY] = account.username
            preferences[EMAIL_KEY] = account.email
            preferences[PROFILE_KEY] = account.profile.toString()
            preferences[TOKEN_KEY] = account.token
            preferences[STATE_KEY] = account.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val ID_KEY = stringPreferencesKey("id")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PROFILE_KEY = stringPreferencesKey("profile")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}