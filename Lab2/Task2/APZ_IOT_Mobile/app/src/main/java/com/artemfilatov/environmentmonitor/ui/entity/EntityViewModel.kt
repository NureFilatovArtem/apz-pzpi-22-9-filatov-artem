package com.artemfilatov.environmentmonitor.ui.entity

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemfilatov.environmentmonitor.data.api.ApiService
import com.artemfilatov.environmentmonitor.data.model.*
import kotlinx.coroutines.launch

class EntityViewModel(private val api: ApiService) : ViewModel() {

    var offices by mutableStateOf(listOf<Office>())
    var buildings by mutableStateOf(listOf<Building>())
    var sensors by mutableStateOf(listOf<Sensor>())
    var subscriptions by mutableStateOf(listOf<Subscription>())

    // === General Util ===
    fun extractIdSafe(input: String, prefix: String = "ID:"): Int {
        return input.substringAfter(prefix).substringBefore(",").trim().toIntOrNull() ?: -1
    }

    // === OFFICES ===
    fun loadOffices() = viewModelScope.launch {
        val response = api.getAllOffices()
        if (response.isSuccessful) {
            offices = response.body() ?: emptyList()
        }
    }

    fun createOffice(fields: Map<String, String>) = viewModelScope.launch {
        val newOffice = Office(
            id = 0,
            name = fields["name"] ?: "",
            buildingId = fields["buildingId"]?.toIntOrNull() ?: 0
        )
        val response = api.createOffice(newOffice)
        if (response.isSuccessful) loadOffices()
    }

    fun editOffice(id: Int, fields: Map<String, String>) = viewModelScope.launch {
        val office = offices.find { it.id == id } ?: return@launch
        val updated = office.copy(
            name = fields["name"] ?: office.name,
            buildingId = fields["buildingId"]?.toIntOrNull() ?: office.buildingId
        )
        val response = api.updateOffice(id, updated)
        if (response.isSuccessful) loadOffices()
    }

    fun deleteOffice(id: Int) = viewModelScope.launch {
        val response = api.deleteOffice(id)
        if (response.isSuccessful) loadOffices()
    }

    // === BUILDINGS ===
    fun loadBuildings() = viewModelScope.launch {
        val response = api.getAllBuildings()
        if (response.isSuccessful) {
            buildings = response.body() ?: emptyList()
        }
    }

    fun createBuilding(fields: Map<String, String>) = viewModelScope.launch {
        val newBuilding = Building(
            id = 0,
            name = fields["name"] ?: "",
            address = fields["address"] ?: ""
        )
        val response = api.createBuilding(newBuilding)
        if (response.isSuccessful) loadBuildings()
    }

    fun editBuilding(id: Int, fields: Map<String, String>) = viewModelScope.launch {
        val building = buildings.find { it.id == id } ?: return@launch
        val updated = building.copy(
            name = fields["name"] ?: building.name,
            address = fields["address"] ?: building.address
        )
        val response = api.updateBuilding(id, updated)
        if (response.isSuccessful) loadBuildings()
    }

    fun deleteBuilding(id: Int) = viewModelScope.launch {
        val response = api.deleteBuilding(id)
        if (response.isSuccessful) loadBuildings()
    }

    // === SENSORS ===
    fun loadSensors() = viewModelScope.launch {
        val response = api.getAllSensors()
        if (response.isSuccessful) {
            sensors = response.body() ?: emptyList()
        }
    }

    fun createSensor(fields: Map<String, String>) = viewModelScope.launch {
        val newSensor = Sensor(
            id = 0,
            type = fields["type"] ?: "",
            officeId = fields["officeId"]?.toIntOrNull()
        )
        val response = api.createSensor(newSensor)
        if (response.isSuccessful) loadSensors()
    }

    fun editSensor(id: Int, fields: Map<String, String>) = viewModelScope.launch {
        val sensor = sensors.find { it.id == id } ?: return@launch
        val updated = sensor.copy(
            type = fields["type"] ?: sensor.type,
            officeId = fields["officeId"]?.toIntOrNull() ?: sensor.officeId
        )
        val response = api.updateSensor(id, updated)
        if (response.isSuccessful) loadSensors()
    }

    fun deleteSensor(id: Int) = viewModelScope.launch {
        val response = api.deleteSensor(id)
        if (response.isSuccessful) loadSensors()
    }

    // === SUBSCRIPTIONS ===
    fun loadSubscriptions() = viewModelScope.launch {
        val response = api.getAllSubscriptions()
        if (response.isSuccessful) {
            subscriptions = response.body() ?: emptyList()
        }
    }

    fun createSubscriptionFromFields(fields: Map<String, String>) = viewModelScope.launch {
        val newSub = Subscription(
            sensor_id = fields["sensor_id"]?.toIntOrNull() ?: 0,
            callback_url = fields["callback_url"] ?: "",
            created_at = fields["created_at"] ?: "2025-01-01T00:00",
            updated_at = fields["updated_at"] ?: "2025-01-01T00:00"
        )
        val response = api.createSubscription(newSub)
        if (response.isSuccessful) loadSubscriptions()
    }

    fun editSubscription(id: Int, fields: Map<String, String>) = viewModelScope.launch {
        val sub = subscriptions.find { it.sensor_id == id } ?: return@launch
        val updated = sub.copy(
            callback_url = fields["callback_url"] ?: sub.callback_url,
            updated_at = fields["updated_at"] ?: sub.updated_at
        )
        val response = api.updateSubscription(id, updated)
        if (response.isSuccessful) loadSubscriptions()
    }

    fun deleteSubscription(id: Int) = viewModelScope.launch {
        val response = api.deleteSubscription(id)
        if (response.isSuccessful) loadSubscriptions()
    }
}
