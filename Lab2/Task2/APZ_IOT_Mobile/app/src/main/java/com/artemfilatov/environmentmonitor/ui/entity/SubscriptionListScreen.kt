package com.artemfilatov.environmentmonitor.ui.entity

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemfilatov.environmentmonitor.data.model.Subscription
import kotlinx.coroutines.launch

@Composable
fun SubscriptionListScreen(
    subscriptions: List<Subscription>,
    onDelete: (String) -> Unit = {},
    onEdit: (String) -> Unit = {},
    onCreateEntity: (Map<String, String>) -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var recentlyDeleted by remember { mutableStateOf<Subscription?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🔔 Підписки", style = MaterialTheme.typography.headlineMedium)
                Button(onClick = {
                    onCreateEntity(
                        mapOf(
                            "sensor_id" to "1",
                            "callback_url" to "https://your.callback",
                            "created_at" to "2025-01-01T00:00",
                            "updated_at" to "2025-01-01T00:00"
                        )
                    )
                }) {
                    Text("+ Додати")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            subscriptions.forEach {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Sensor ID: ${it.sensor_id}")
                            Text("Callback: ${it.callback_url}")
                            Text("Created: ${it.created_at}")
                            Text("Updated: ${it.updated_at}")
                        }
                        Row {
                            IconButton(onClick = {
                                try {
                                    onEdit(it.sensor_id.toString())
                                } catch (e: Exception) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("⚠ Помилка редагування")
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = {
                                recentlyDeleted = it
                                onDelete(it.sensor_id.toString())
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Підписку видалено",
                                        actionLabel = "Відміна"
                                    )
                                    if (result == SnackbarResult.ActionPerformed && recentlyDeleted != null) {
                                        onCreateEntity(
                                            mapOf(
                                                "sensor_id" to recentlyDeleted!!.sensor_id.toString(),
                                                "callback_url" to recentlyDeleted!!.callback_url,
                                                "created_at" to recentlyDeleted!!.created_at,
                                                "updated_at" to recentlyDeleted!!.updated_at
                                            )
                                        )
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
