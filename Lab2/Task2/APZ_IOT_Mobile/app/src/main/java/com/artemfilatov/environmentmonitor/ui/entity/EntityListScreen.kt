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
import kotlinx.coroutines.launch

@Composable
fun EntityListScreen(
    title: String,
    entityType: String,
    items: List<String>,
    onDelete: (Map<String, String>) -> Unit, // ✅ було (String)
    onEdit: (Map<String, String>) -> Unit,   // ✅ було (String)
    onCreateEntity: (Map<String, String>) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var currentEditItem by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isEditMode = false
                currentEditItem = null
                showDialog = true
            }) {
                Text("+")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
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
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = item)
                        Row {
                            IconButton(onClick = {
                                isEditMode = true
                                currentEditItem = item
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }

                            IconButton(onClick = {
                                val deletedMap = when (entityType) {
                                    "office" -> mapOf("name" to item, "buildingId" to "restored")
                                    "building" -> mapOf("name" to item, "address" to "restored")
                                    "sensor" -> mapOf("type" to "restored", "officeId" to "restored")
                                    "subscription" -> mapOf("sensor_id" to "1", "callback_url" to "restored")
                                    else -> emptyMap()
                                }

                                onDelete(deletedMap)

                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Запис видалено",
                                        actionLabel = "Скасувати"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        onCreateEntity(deletedMap)
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
    if (showDialog) {
        CreateEntityDialog(
            entityType = entityType,
            isEdit = isEditMode,
            defaultValues = currentEditItem?.let {
                when (entityType) {
                    "office", "building" -> mapOf("field1" to it, "field2" to "")
                    "sensor" -> mapOf(
                        "field1" to it.substringAfter("Type: ").substringBefore(","),
                        "field2" to ""
                    )

                    "subscription" -> mapOf(
                        "field1" to it.substringAfter("Sensor ID: ").substringBefore(","),
                        "field2" to ""
                    )

                    else -> emptyMap()
                }
            } ?: emptyMap(),
            onDismiss = { showDialog = false },
            onSubmit = { fields ->
                if (isEditMode && currentEditItem != null) {
                    onEdit(fields)
                } else {
                    onCreateEntity(fields)
                }
                showDialog = false
            }
        )
    }
}
