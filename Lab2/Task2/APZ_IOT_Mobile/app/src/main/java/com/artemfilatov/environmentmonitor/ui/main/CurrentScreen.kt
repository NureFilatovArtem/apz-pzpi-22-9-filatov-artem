package com.artemfilatov.environmentmonitor.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemfilatov.environmentmonitor.data.model.Measurement
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CurrentScreen(latestMeasurements: List<Measurement>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "📟 Поточні параметри",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (latestMeasurements.isEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Завантаження даних з API...")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(latestMeasurements) { measurement ->
                    MeasurementCard(measurement)
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun MeasurementCard(measurement: Measurement) {
    val formattedDate = try {
        ZonedDateTime.parse(measurement.timestamp)
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    } catch (e: Exception) {
        measurement.timestamp
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🔢 Значення: ${measurement.value} ${measurement.unit}", style = MaterialTheme.typography.bodyLarge)
            Text("📡 Сенсор: ${measurement.sensorId}", style = MaterialTheme.typography.labelLarge)
            Text("⏱ Час: $formattedDate", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
