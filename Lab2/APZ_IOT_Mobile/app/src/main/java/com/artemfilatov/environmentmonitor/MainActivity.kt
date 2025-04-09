package com.artemfilatov.environmentmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.artemfilatov.environmentmonitor.data.repository.MeasurementRepository
import com.artemfilatov.environmentmonitor.ui.navigation.NavGraph
import com.artemfilatov.environmentmonitor.ui.theme.Enviroment_ApplicationTheme
import com.artemfilatov.environmentmonitor.utils.RetrofitInstance
import com.artemfilatov.environmentmonitor.viewmodel.MainViewModel
import com.artemfilatov.environmentmonitor.viewmodel.MainViewModelFactory
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Enviroment_ApplicationTheme {
                val navController = rememberNavController()

                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(
                        MeasurementRepository(RetrofitInstance.api)
                    )
                )

                NavGraph(navController = navController, viewModel = viewModel)
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
    Enviroment_ApplicationTheme {
        Greeting("Android")
    }
}