// ui/main/MainScreen.kt
package com.artemfilatov.environmentmonitor.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.artemfilatov.environmentmonitor.ui.components.BottomNavBar
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment


@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\u0413\u043e\u043b\u043e\u0432\u043d\u0438\u0439 \u0435\u043a\u0440\u0430\u043d",
                style = MaterialTheme.typography.headlineMedium
            )

            DashboardGrid(navController)
        }
    }
}

@Composable
fun DashboardGrid(navController: NavController) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DashboardItem("\ud83c\udfe2 Офіси", "offices", navController, Modifier.weight(1f))
            DashboardItem("\ud83c\udfe0 Будівлі", "buildings", navController, Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DashboardItem("\ud83d\udce1 Сенсори", "sensors", navController, Modifier.weight(1f))
            DashboardItem("\ud83d\udd14 Підписки", "subscriptions", navController, Modifier.weight(1f))
        }
    }
}

@Composable
fun DashboardItem(label: String, route: String, navController: NavController, modifier: Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { navController.navigate(route) },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

