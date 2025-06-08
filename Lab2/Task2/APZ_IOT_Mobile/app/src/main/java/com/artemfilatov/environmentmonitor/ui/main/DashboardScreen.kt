package com.artemfilatov.environmentmonitor.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📊 Огляд системи",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        DashboardGrid(onNavigate)
    }
}

@Composable
fun DashboardGrid(onNavigate: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DashboardItem("🏢 Офіси", "offices", onNavigate, modifier = Modifier.weight(1f))
            DashboardItem("🏬 Будівлі", "buildings", onNavigate, modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DashboardItem("📡 Сенсори", "sensors", onNavigate, modifier = Modifier.weight(1f))
            DashboardItem("🔔 Підписки", "subscriptions", onNavigate, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun DashboardItem(
    label: String,
    route: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier // <-- додано!
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onNavigate(route) },
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
