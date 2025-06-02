package com.artemfilatov.environmentmonitor.ui.entity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CreateEntityDialog(
    entityType: String,
    defaultValues: Map<String, String> = emptyMap(),
    isEdit: Boolean = false,
    onSubmit: (Map<String, String>) -> Unit,
    onDismiss: () -> Unit
) {
    var field1 by remember { mutableStateOf(defaultValues["field1"] ?: "") }
    var field2 by remember { mutableStateOf(defaultValues["field2"] ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val data = when (entityType) {
                    "office" -> mapOf("name" to field1, "buildingId" to field2)
                    "building" -> mapOf("name" to field1, "address" to field2)
                    "sensor" -> mapOf("type" to field1, "officeId" to field2)
                    "subscription" -> mapOf("sensor_id" to field1, "callback_url" to field2)
                    else -> emptyMap()
                }
                onSubmit(data)
            }) {
                Text(if (isEdit) "Оновити" else "Створити")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Скасувати")
            }
        },
        title = {
            Text(if (isEdit) "✏️ Редагування" else "➕ Створення")
        },
        text = {
            Column {
                when (entityType) {
                    "office" -> {
                        OutlinedTextField(
                            value = field1,
                            onValueChange = { field1 = it },
                            label = { Text("Назва") },
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = field2,
                            onValueChange = { field2 = it },
                            label = { Text("ID будівлі") }
                        )
                    }
                    "building" -> {
                        OutlinedTextField(
                            value = field1,
                            onValueChange = { field1 = it },
                            label = { Text("Назва") },
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = field2,
                            onValueChange = { field2 = it },
                            label = { Text("Адреса") }
                        )
                    }
                    "sensor" -> {
                        OutlinedTextField(
                            value = field1,
                            onValueChange = { field1 = it },
                            label = { Text("Тип") },
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = field2,
                            onValueChange = { field2 = it },
                            label = { Text("ID офісу") }
                        )
                    }
                    "subscription" -> {
                        OutlinedTextField(
                            value = field1,
                            onValueChange = { field1 = it },
                            label = { Text("Sensor ID") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = field2,
                            onValueChange = { field2 = it },
                            label = { Text("Callback URL") }
                        )
                    }
                }
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}
