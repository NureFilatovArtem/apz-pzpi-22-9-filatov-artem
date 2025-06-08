package com.artemfilatov.iotsensors.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artemfilatov.iotsensors.api.RetrofitClient
import com.artemfilatov.iotsensors.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onLogout: () -> Unit) {
    val context = LocalContext.current
    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory(RetrofitClient.create(context))
    )

    // Спостерігаємо за ВСІМА даними, включаючи measurements
    val buildings by dashboardViewModel.buildings.observeAsState(emptyList())
    val sensors by dashboardViewModel.sensors.observeAsState(emptyList())
    val offices by dashboardViewModel.offices.observeAsState(emptyList())
    val measurements by dashboardViewModel.measurements.observeAsState(emptyList()) // <-- ДОДАНО
    val isLoading by dashboardViewModel.isLoading.observeAsState(false)
    val error by dashboardViewModel.error.observeAsState()

    // Завантажуємо дані один раз при вході на екран
    LaunchedEffect(Unit) {
        dashboardViewModel.loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = { dashboardViewModel.loadData() }, enabled = !isLoading) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Open Add Dialog */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Показуємо індикатор завантаження тільки при першому вході
            if (isLoading && measurements.isEmpty() && buildings.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error?.let {
                // Покращене відображення помилки
                Column(modifier = Modifier.align(Alignment.Center).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(it, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { dashboardViewModel.loadData() }, enabled = !isLoading) {
                        Text("Retry")
                    }
                }
            }

            // LazyColumn для прокручування всього контенту
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- ДОДАНА СЕКЦІЯ ДЛЯ ВИМІРІВ (ПЕРША В СПИСКУ!) ---
                item {
                    Text("Latest Measurements", style = MaterialTheme.typography.headlineSmall)
                }
                items(measurements) { measurement ->
                    DataCard(item = measurement)
                }

                // --- СТАРІ СЕКЦІЇ, ЩО ЗАЛИШИЛИСЬ БЕЗ ЗМІН ---
                item {
                    Text("Buildings", style = MaterialTheme.typography.headlineSmall)
                }
                items(buildings) { building ->
                    DataCard(item = building)
                }

                item {
                    Text("Sensors", style = MaterialTheme.typography.headlineSmall)
                }
                items(sensors) { sensor ->
                    DataCard(item = sensor)
                }

                item {
                    Text("Offices", style = MaterialTheme.typography.headlineSmall)
                }
                items(offices) { office ->
                    DataCard(item = office)
                }
            }
        }
    }
}

// Універсальна картка (додано відображення для Measurement)
@Composable
fun DataCard(item: Any) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            // --- ОСЬ ТУТ ДОДАНА ЛОГІКА ДЛЯ MEASUREMENT ---
            when (item) {
                is Measurement -> {
                    Text("Measurement #${item.id}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("From Sensor ID: ${item.sensorId}")
                    Text("Value: ${item.value} ${item.unit}", style = MaterialTheme.typography.bodyLarge)
                    // Спрощене відображення дати
                    Text(
                        text = "At: ${item.timestamp.replace("T", " ").substring(0, 19)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
                // --- СТАРА ЛОГІКА ЗАЛИШИЛАСЬ ---
                is Building -> {
                    Text("Building #${item.id}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("Name: ${item.name}")
                    item.address?.let { Text("Address: $it") }
                }
                is Sensor -> {
                    Text("Sensor #${item.id}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("Type: ${item.type}")
                    item.buildingId?.let { Text("Building ID: $it") }
                }
                is Office -> {
                    Text("Office #${item.id}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("Name: ${item.name}")
                    item.location?.let { Text("Location: $it") }
                }
            }
        }
    }
}