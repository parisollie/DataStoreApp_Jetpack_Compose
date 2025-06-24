package com.pjff.datastoreapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// V-93,paso 1.6 , el contexto nos permite a entrar a diferentes recursos.
class StoreUserEmail(private val context: Context) {

    // Nos permite definir miembros sin crear clases.
    companion object {
        //UserEmail , no es la clave valor
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserEmail")
        // Este es el id del registro
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    // Para mandar a llamar la variable usamos el flow.
    val getEmail: Flow<String> = context.dataStore.data
        .map { preferences ->
            //Ponemos un dato por defecto ?: ""
            preferences[USER_EMAIL] ?: ""
        }

    // Para usar una función dentro dentro de una corrutina usamos supend.
    suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

}