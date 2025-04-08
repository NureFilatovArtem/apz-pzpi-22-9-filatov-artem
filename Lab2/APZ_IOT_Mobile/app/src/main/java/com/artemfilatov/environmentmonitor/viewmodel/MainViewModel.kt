package com.artemfilatov.environmentmonitor.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemfilatov.environmentmonitor.data.model.Measurement
import com.artemfilatov.environmentmonitor.data.repository.MeasurementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repo: MeasurementRepository) : ViewModel() {

    private val _latestMeasurements = MutableStateFlow<List<Measurement>>(emptyList())
    val latestMeasurements: StateFlow<List<Measurement>> = _latestMeasurements

    fun loadLatestMeasurements() {
        viewModelScope.launch {
            try {
                val result = repo.getLatest()
                Log.d("MainVM", "✅ Latest loaded: ${result.size}")
                _latestMeasurements.value = result
            } catch (e: Exception) {
                Log.e("MainVM", "❌ API failed", e)
            }
        }
    }
}