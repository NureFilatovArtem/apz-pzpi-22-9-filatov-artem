package com.artemfilatov.iotsensors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artemfilatov.iotsensors.auth.LoginViewModel
import com.artemfilatov.iotsensors.ui.dashboard.DashboardScreen
import com.artemfilatov.iotsensors.ui.login.LoginScreen
import com.artemfilatov.iotsensors.ui.theme.IOTSensorsTheme
import com.artemfilatov.iotsensors.utils.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IOTSensorsTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val context = LocalContext.current
    val token = TokenManager.getToken(context)

    if (token.isNullOrBlank()) {
        // --- ПРАВИЛЬНИЙ ВИКЛИК VIEWMODEL З ФАБРИКОЮ ---
        val loginViewModel: LoginViewModel = viewModel(
            factory = LoginViewModel.Factory(context.applicationContext as android.app.Application)
        )
        LoginScreen(
            viewModel = loginViewModel,
            onLoginSuccess = {
                // "Перезавантажуємо" UI, щоб показати дашборд
                (context as? ComponentActivity)?.recreate()
            }
        )
    } else {
        DashboardScreen(
            onLogout = {
                TokenManager.clearToken(context)
                (context as? ComponentActivity)?.recreate() // Перезавантажуємо, щоб показати логін
            }
        )
    }
}