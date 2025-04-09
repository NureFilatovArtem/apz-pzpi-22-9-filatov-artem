package com.artemfilatov.environmentmonitor.ui.entity

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemfilatov.environmentmonitor.data.model.Subscription

@Composable
fun SubscriptionListScreen(subscriptions: List<Subscription>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "🔔 Підписки", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        subscriptions.forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sensor ID: ${it.sensor_id}")
                    Text("Callback: ${it.callback_url}")
                    Text("Created: ${it.created_at}")
                    Text("Updated: ${it.updated_at}")
                }
            }
        }
    }
}
