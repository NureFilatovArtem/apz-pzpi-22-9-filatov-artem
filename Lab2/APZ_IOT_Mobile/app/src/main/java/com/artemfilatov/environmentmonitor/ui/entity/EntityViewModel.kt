package com.artemfilatov.environmentmonitor.ui.entity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemfilatov.environmentmonitor.data.api.ApiService
import com.artemfilatov.environmentmonitor.data.model.Building
import com.artemfilatov.environmentmonitor.data.model.Office
import com.artemfilatov.environmentmonitor.data.model.Sensor
import com.artemfilatov.environmentmonitor.data.model.Subscription
import kotlinx.coroutines.launch

class EntityViewModel(private val api: ApiService) : ViewModel() {

    var offices by mutableStateOf(listOf<Office>())
    var buildings by mutableStateOf(listOf<Building>())
    var sensors by mutableStateOf(listOf<Sensor>())
    var subscriptions by mutableStateOf(listOf<Subscription>())

    // === OFFICES ===
    fun loadOffices() = viewModelScope.launch {
        offices = api.getAllOffices()
    }

    fun deleteOfficeByName(name: String) = viewModelScope.launch {
        val office = offices.find { it.name == name } ?: return@launch
        deleteOffice(office.id)
    }

    fun editOffice(name: String, fields: Map<String, String>) = viewModelScope.launch {
        val office = offices.find { it.name == name } ?: return@launch
        val updated = office.copy(
            name = fields["name"] ?: office.name,
            buildingId = fields["buildingId"] ?: office.buildingId
        )
        updateOffice(office.id, updated)
    }

    fun createOffice(fields: Map<String, String>) = viewModelScope.launch {
        val newOffice = Office(
            id = "",
            name = fields["name"] ?: "No name",
            buildingId = fields["buildingId"] ?: ""
        )
        createOffice(newOffice)
    }

    fun createOffice(office: Office) = viewModelScope.launch {
        val response = api.createOffice(office)
        if (response.isSuccessful) loadOffices()
    }

    fun updateOffice(id: String, office: Office) = viewModelScope.launch {
        val response = api.updateOffice(id, office)
        if (response.isSuccessful) loadOffices()
    }

    fun deleteOffice(id: String) = viewModelScope.launch {
        val response = api.deleteOffice(id)
        if (response.isSuccessful) loadOffices()
    }

    // === BUILDINGS ===
    fun loadBuildings() = viewModelScope.launch {
        buildings = api.getAllBuildings()
    }

    fun deleteBuildingByName(name: String) = viewModelScope.launch {
        val building = buildings.find { it.name == name } ?: return@launch
        deleteBuilding(building.id)
    }

    fun editBuilding(name: String, fields: Map<String, String>) = viewModelScope.launch {
        val building = buildings.find { it.name == name } ?: return@launch
        val updated = building.copy(
            name = fields["name"] ?: building.name,
            address = fields["address"] ?: building.address
        )
        updateBuilding(building.id, updated)
    }

    fun createBuilding(fields: Map<String, String>) = viewModelScope.launch {
        val newBuilding = Building(
            id = "",
            name = fields["name"] ?: "No name",
            address = fields["address"] ?: ""
        )
        createBuilding(newBuilding)
    }

    fun createBuilding(building: Building) = viewModelScope.launch {
        val response = api.createBuilding(building)
        if (response.isSuccessful) loadBuildings()
    }

    fun updateBuilding(id: String, building: Building) = viewModelScope.launch {
        val response = api.updateBuilding(id, building)
        if (response.isSuccessful) loadBuildings()
    }

    fun deleteBuilding(id: String) = viewModelScope.launch {
        val response = api.deleteBuilding(id)
        if (response.isSuccessful) loadBuildings()
    }

    // === SENSORS ===
    fun loadSensors() = viewModelScope.launch {
        sensors = api.getAllSensors()
    }

    fun deleteSensorById(sensorInfo: String) = viewModelScope.launch {
        val id = sensorInfo.substringAfter("ID: ").substringBefore(",").trim()
        deleteSensor(id)
    }

    fun editSensorById(sensorInfo: String, fields: Map<String, String>) = viewModelScope.launch {
        val id = sensorInfo.substringAfter("ID: ").substringBefore(",").trim()
        val sensor = sensors.find { it.id == id } ?: return@launch
        val updated = sensor.copy(
            type = fields["type"] ?: sensor.type,
            officeId = fields["officeId"] ?: sensor.officeId
        )
        updateSensor(sensor.id, updated)
    }

    fun extractIdSafe(input: String, prefix: String = "ID:"): String {
        return try {
            input.substringAfter(prefix).substringBefore(",").trim()
        } catch (e: Exception) {
            ""
        }
    }



    fun editSubscription(id: String, fields: Map<String, String>) = viewModelScope.launch {
        val updated = Subscription(
            sensor_id = fields["sensor_id"]?.toIntOrNull() ?: 0,
            callback_url = fields["callback_url"] ?: "",
            created_at = "2025-01-01T00:00", // або витягни з бекенду
            updated_at = "2025-04-10T00:00"
        )
        updateSubscription(id, updated)
    }

    fun createSensor(fields: Map<String, String>) = viewModelScope.launch {
        val newSensor = Sensor(
            id = "",
            type = fields["type"] ?: "unknown",
            officeId = fields["officeId"] ?: ""
        )
        createSensor(newSensor)
    }

    fun createSensor(sensor: Sensor) = viewModelScope.launch {
        val response = api.createSensor(sensor)
        if (response.isSuccessful) loadSensors()
    }

    fun updateSensor(id: String, sensor: Sensor) = viewModelScope.launch {
        val response = api.updateSensor(id, sensor)
        if (response.isSuccessful) loadSensors()
    }

    fun deleteSensor(id: String) = viewModelScope.launch {
        val response = api.deleteSensor(id)
        if (response.isSuccessful) loadSensors()
    }

    // === SUBSCRIPTIONS ===
    fun loadSubscriptions() = viewModelScope.launch {
        subscriptions = api.getAllSubscriptions()
    }

    fun deleteSubscriptionBySensorId(sensorInfo: String) = viewModelScope.launch {
        val id = sensorInfo.substringAfter("ID: ").substringBefore(",").trim()
        deleteSubscription(id)
    }

    fun editSubscriptionBySensorId(sensorInfo: String, fields: Map<String, String>) = viewModelScope.launch {
        val id = sensorInfo.substringAfter("Sensor ID: ").substringBefore(",").trim()
        val sub = subscriptions.find { it.sensor_id.toString() == id } ?: return@launch
        val updated = sub.copy(
            callback_url = fields["callback_url"] ?: sub.callback_url,
            created_at = fields["created_at"] ?: sub.created_at,
            updated_at = fields["updated_at"] ?: sub.updated_at
        )
        updateSubscription(id, updated)
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

    fun updateSubscription(id: String, subscription: Subscription) = viewModelScope.launch {
        val response = api.updateSubscription(id, subscription)
        if (response.isSuccessful) loadSubscriptions()
    }

    fun deleteSubscription(id: String) = viewModelScope.launch {
        val response = api.deleteSubscription(id)
        if (response.isSuccessful) loadSubscriptions()
    }
}