package com.artemfilatov.environmentmonitor.ui.entity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EntityListScreen(
    title: String,
    entityType: String,
    items: List<String>,
    onEdit: (Map<String, String>) -> Unit = {},
    onDelete: (String) -> Unit = {},
    onCreateEntity: (Map<String, String>) -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var defaultFields by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            items.forEach { item ->
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
                        Text(text = item)

                        Row {
                            IconButton(onClick = {
                                isEditMode = true
                                defaultFields = when (entityType) {
                                    "office" -> mapOf(
                                        "field1" to item,
                                        "field2" to "" // ID будівлі – можна отримувати окремо
                                    )
                                    "building" -> mapOf("field1" to item, "field2" to "")
                                    "sensor" -> mapOf("field1" to item, "field2" to "")
                                    "subscription" -> mapOf("field1" to item, "field2" to "")
                                    else -> emptyMap()
                                }
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }

                            IconButton(onClick = { onDelete(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }

        // ➕ Додати (FAB)
        FloatingActionButton(
            onClick = {
                isEditMode = false
                defaultFields = emptyMap()
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }

        // 🧩 Діалог (Edit / Create)
        if (showDialog) {
            CreateEntityDialog(
                entityType = entityType,
                defaultValues = defaultFields,
                isEdit = isEditMode,
                onDismiss = { showDialog = false },
                onSubmit = {
                    if (isEditMode) onEdit(it) else onCreateEntity(it)
                    showDialog = false
                }
            )
        }
    }
}
