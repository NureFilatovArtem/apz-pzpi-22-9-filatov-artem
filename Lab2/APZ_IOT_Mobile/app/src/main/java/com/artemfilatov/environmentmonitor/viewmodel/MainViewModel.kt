package com.artemfilatov.environmentmonitor.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemfilatov.environmentmonitor.data.model.Measurement
import com.artemfilatov.environmentmonitor.data.repository.MeasurementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MeasurementState {
    object Loading : MeasurementState()
    data class Success(val measurements: List<Measurement>) : MeasurementState()
    data class Error(val message: String) : MeasurementState()
}

class MainViewModel(private val repo: MeasurementRepository) : ViewModel() {

    private val _measurementState = MutableStateFlow<MeasurementState>(MeasurementState.Loading)
    val measurementState: StateFlow<MeasurementState> = _measurementState

    init {
        loadLatestMeasurements()
    }

    fun loadLatestMeasurements() {
        viewModelScope.launch {
            _measurementState.value = MeasurementState.Loading
            try {
                val result = repo.getLatest()
                Log.d("MainVM", "✅ Latest loaded: ${result.size}")
                _measurementState.value = MeasurementState.Success(result)
            } catch (e: Exception) {
                Log.e("MainVM", "❌ API failed", e)
                _measurementState.value = MeasurementState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}