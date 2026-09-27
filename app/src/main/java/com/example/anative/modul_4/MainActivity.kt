package com.example.anative.modul_4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.anative.modul_4.ui.theme.NativeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NativeTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                var number by rememberSaveable { mutableIntStateOf(1) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) {

                        innerPadding ->
                    CounterScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(32.dp),
                        number = number,
                        label = "Double",
                        onButtonClick = { number *= 2 }
                    )
                    LaunchedEffect(number) {
                        snackbarHostState.showSnackbar("Number $number is shown!")
                    }
                    LaunchedEffect(Unit) {
                        number = Repo.getData()
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        NativeTheme {
            Greeting("Android")
        }
    }

    @Composable
    fun CounterScreen(modifier: Modifier, number: Int, label: String, onButtonClick: () -> Unit) {
        Column(modifier = modifier) {
            Text(text = "$number", fontSize = 72.sp)
            Button(onClick = onButtonClick) {
                Text(text = label)
            }
        }
    }
}
