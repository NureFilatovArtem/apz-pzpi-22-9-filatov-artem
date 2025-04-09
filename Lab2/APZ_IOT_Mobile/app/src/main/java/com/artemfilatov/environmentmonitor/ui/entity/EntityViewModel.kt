package com.artemfilatov.environmentmonitor.ui.entity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemfilatov.environmentmonitor.data.api.ApiService
import com.artemfilatov.environmentmonitor.data.model.Building
import com.artemfilatov.environmentmonitor.data.model.Office
import com.artemfilatov.environmentmonitor.data.model.Sensor
import com.artemfilatov.environmentmonitor.data.model.Subscription
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class EntityViewModel(private val api: ApiService) : ViewModel() {
    var offices by mutableStateOf(listOf<Office>())
    var buildings by mutableStateOf(listOf<Building>())
    var sensors by mutableStateOf(listOf<Sensor>())
    var subscriptions by mutableStateOf(listOf<Subscription>())

    fun loadOffices() = viewModelScope.launch {
        offices = api.getAllOffices()
    }

    fun loadBuildings() = viewModelScope.launch {
        buildings = api.getAllBuildings()
    }

    fun loadSensors() = viewModelScope.launch {
        sensors = api.getAllSensors()
    }

    fun loadSubscriptions() = viewModelScope.launch {
        subscriptions = api.getAllSubscriptions()
    }
}
