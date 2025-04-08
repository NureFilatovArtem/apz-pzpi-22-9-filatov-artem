package com.artemfilatov.environmentmonitor.ui.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemfilatov.environmentmonitor.data.model.Building
import com.artemfilatov.environmentmonitor.data.model.Office
import com.artemfilatov.environmentmonitor.data.model.Sensor

@Composable
fun DataOverviewScreen(
    buildings: List<Building>,
    offices: List<Office>,
    sensors: List<Sensor>,
    onRefresh: () -> Unit // <-- нове
) {
    val isLoading = buildings.isEmpty() && offices.isEmpty() && sensors.isEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("📊 Огляд системи", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRefresh) {
            Text("🔁 Оновити дані")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
            Text("Завантаження даних з API...")
        } else {
            Text("🏢 Будівлі:")
            buildings.forEach {
                Text("- ${it.name}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("🏬 Офіси:")
            offices.forEach {
                Text("- ${it.name}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("📡 Сенсори:")
            sensors.forEach {
                Text("- Sensor ID: ${it.id}, Type: ${it.type}")
            }
        }
    }
}

