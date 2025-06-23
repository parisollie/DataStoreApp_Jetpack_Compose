package com.pjff.datastoreapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


//Vid 93,el contexto nos permite a entrar a diferentes recursos.
class StoreUserEmail(private val context: Context) {

    //Vid 93, nos permite definir miembros sin crear clases
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserEmail")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    //Para mandar a llamar la variable usamos el flow
    val getEmail: Flow<String> = context.dataStore.data
        .map { preferences ->
            //Ponemos un dato por defecto ?: ""
            preferences[USER_EMAIL] ?: ""
        }


    //Para usar una funcion dentro de una ecorrutina usamos supend
    suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }


}