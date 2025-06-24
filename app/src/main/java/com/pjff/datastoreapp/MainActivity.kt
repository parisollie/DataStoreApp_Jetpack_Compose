package com.pjff.datastoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pjff.datastoreapp.ui.theme.DataStoreAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //Paso 3.1,el contexto de aqui es this
            val darkModeStore = StoreDarkMode(this)
            val darkMode = darkModeStore.getDarkMode.collectAsState(initial = false)
            DataStoreAppTheme(
                darkTheme = darkMode.value
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Paso 3.3, le pasamos el darkMode
                    Greeting(darkModeStore, darkMode.value)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Paso 3.2, le ponemos el darkModeStore
fun Greeting(darkModeStore: StoreDarkMode, darkMode: Boolean) {
    //V-94,Paso 2.0 sacamos el contexto
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreUserEmail(context)

    //Paso 1.1
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        /*
          Paso 1.2,ponemos la variable email.
          V-92, paso 1.5, agregamos el rememberSaveablepara que al girar la pantalla no se pierda el valor.
        */
        var email by rememberSaveable { mutableStateOf("") }
        //Paso 2.1, mandamos a llamar el dato
        val userEmail = dataStore.getEmail.collectAsState(initial = "")

        //Agregamos el textfield para poner nuestr correo
        TextField(
            value = email,
            onValueChange = { email = it },
            //Paso 1.3, ponemos el keyboard tipo email.
            keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Email)
            )
        Spacer(modifier = Modifier.height(25.dp))
        //Paso 1.4, ponemos nuestro botón
        Button(onClick = {
            // Paso 2.3 accedemos a al corrutina
            scope.launch {
                //Ponemos el email guardado
                dataStore.saveEmail(email)
            }
        }) {
            Text("Guardar Email")
        }
        Spacer(modifier = Modifier.height(16.dp))
        //Paso 2.2
        Text(userEmail.value)
        Spacer(modifier = Modifier.height(16.dp))

        //Paso 3.4, agregamos el boton para cambiar el tema
        Button(
            onClick = {
            scope.launch {
                if(darkMode){
                    darkModeStore.saveDarkMode(false)
                }else{
                    darkModeStore.saveDarkMode(true)
                }
            }
        }) {
            Text("Cambiar a dark")
        }//Button

        //Paso 3.5
        Switch(checked = darkMode, onCheckedChange = { isChecked ->
            scope.launch {
                darkModeStore.saveDarkMode(isChecked)
            }
        }) //Switch
    }
}

