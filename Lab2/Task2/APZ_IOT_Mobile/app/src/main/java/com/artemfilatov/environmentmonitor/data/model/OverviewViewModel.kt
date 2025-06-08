package com.artemfilatov.environmentmonitor.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemfilatov.environmentmonitor.data.api.ApiService
import com.artemfilatov.environmentmonitor.data.model.Building
import com.artemfilatov.environmentmonitor.data.model.Office
import com.artemfilatov.environmentmonitor.data.model.Sensor
import com.artemfilatov.environmentmonitor.data.model.Subscription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OverviewViewModel(private val api: ApiService) : ViewModel() {

    private val _offices = MutableStateFlow<List<Office>>(emptyList())
    val offices: StateFlow<List<Office>> = _offices

    private val _buildings = MutableStateFlow<List<Building>>(emptyList())
    val buildings: StateFlow<List<Building>> = _buildings

    private val _sensors = MutableStateFlow<List<Sensor>>(emptyList())
    val sensors: StateFlow<List<Sensor>> = _sensors

    private val _subscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptions: StateFlow<List<Subscription>> = _subscriptions

    fun loadAll() {
        viewModelScope.launch {
            try {
                _offices.value = api.getAllOffices()
                _buildings.value = api.getAllBuildings()
                _sensors.value = api.getAllSensors()
                _subscriptions.value = api.getAllSubscriptions()
            } catch (e: Exception) {
                Log.e("OverviewVM", "Failed to load info about models", e)
            }
        }
    }
}
