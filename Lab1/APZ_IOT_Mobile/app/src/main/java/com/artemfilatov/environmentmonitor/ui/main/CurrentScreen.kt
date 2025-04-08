package com.artemfilatov.environmentmonitor.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Поточні параметри", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        when {
            latestMeasurements.isEmpty() -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Завантаження даних з API...")
            }
            else -> {
                latestMeasurements.forEach { measurement ->
                    MeasurementCard(measurement)
                    Spacer(modifier = Modifier.height(12.dp))
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
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${measurement.unit}: ${measurement.value}")
            Text("Sensor: ${measurement.sensorId}")
            Text("Час: $formattedDate")
        }
    }
}
