// ui/main/MainScreen.kt
package com.artemfilatov.environmentmonitor.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Вітаємо у системі моніторингу середовища!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate("current") }) {
            Text("Поточні параметри")
        }

        Button(onClick = { navController.navigate("overview") }) {
            Text("📊 Огляд системи")
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("history") }) {
            Text("Історія змін")
        }
    }
}
