package com.pjff.datastoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pjff.datastoreapp.ui.theme.DataStoreAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //Vid 95,el contexto de aqui es this
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
                    //Vid 95
                    Greeting(darkModeStore, darkMode.value)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(darkModeStore: StoreDarkMode, darkMode: Boolean) {
    //Vid 94, sacamos el contexto
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreUserEmail(context)

    //Vid 91
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        //Vid 92,ponemos la variable rememberSaveable al momento de girar la pantalla se guarda.
        var email by rememberSaveable { mutableStateOf("") }
        val userEmail = dataStore.getEmail.collectAsState(initial = "")
        //Vid 94,agregamos el textfield para poner nuestr correo
        TextField(
            value = email,
            onValueChange = { email = it },
            //Vid 91, Ponemos el keyboard tipo email.
            keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Email)
            )
        Spacer(modifier = Modifier.height(25.dp))
        //Vid 91,ponemos nuestro boton
        Button(onClick = {
            //vid 94, accedemos a al corrutina
            scope.launch {
                //ponemos el email guardado
                dataStore.saveEmail(email)
            }
        }) {
            Text("Guardar Email")
        }
        Spacer(modifier = Modifier.height(16.dp))
        //Vid 94,
        Text(userEmail.value)
        Spacer(modifier = Modifier.height(16.dp))

        //Vid 95
        Button(onClick = {
            scope.launch {
                if(darkMode){
                    darkModeStore.saveDarkMode(false)
                }else{
                    darkModeStore.saveDarkMode(true)
                }
            }
        }) {
            Text("Cambiar a dark")
        }

        //Vid 95

        Switch(checked = darkMode, onCheckedChange = { isChecked ->
            scope.launch {
                darkModeStore.saveDarkMode(isChecked)
            }
        })

    }
}

