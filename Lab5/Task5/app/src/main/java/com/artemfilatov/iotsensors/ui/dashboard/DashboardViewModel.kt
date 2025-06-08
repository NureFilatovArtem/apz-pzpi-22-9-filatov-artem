package com.artemfilatov.iotsensors.ui.dashboard

import androidx.lifecycle.*
import com.artemfilatov.iotsensors.api.ApiService
import com.artemfilatov.iotsensors.model.*
import kotlinx.coroutines.launch

class DashboardViewModel(private val apiService: ApiService) : ViewModel() {

    // ... існуючі LiveData для sensors, buildings, offices ...
    private val _sensors = MutableLiveData<List<Sensor>>()
    val sensors: LiveData<List<Sensor>> = _sensors
    private val _buildings = MutableLiveData<List<Building>>()
    val buildings: LiveData<List<Building>> = _buildings
    private val _offices = MutableLiveData<List<Office>>()
    val offices: LiveData<List<Office>> = _offices

    // --- ДОДАЄМО LIVADATA ДЛЯ ВИМІРІВ ---
    private val _measurements = MutableLiveData<List<Measurement>>()
    val measurements: LiveData<List<Measurement>> = _measurements

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Завантажуємо все паралельно, включаючи виміри
                val sensorsRes = apiService.getSensors()
                val buildingsRes = apiService.getBuildings()
                val officesRes = apiService.getOffices()
                val measurementsRes = apiService.getMeasurements() // <-- ДОДАНО

                if(sensorsRes.isSuccessful) _sensors.value = sensorsRes.body()
                if(buildingsRes.isSuccessful) _buildings.value = buildingsRes.body()
                if(officesRes.isSuccessful) _offices.value = officesRes.body()
                if(measurementsRes.isSuccessful) _measurements.value = measurementsRes.body() // <-- ДОДАНО

            } catch (e: Exception) {
                _error.value = "Failed to load data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Фабрика залишається без змін
    class Factory(private val apiService: ApiService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(apiService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}