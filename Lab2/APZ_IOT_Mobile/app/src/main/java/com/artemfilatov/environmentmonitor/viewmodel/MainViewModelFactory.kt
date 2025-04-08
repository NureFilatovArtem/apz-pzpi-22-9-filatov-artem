package com.artemfilatov.environmentmonitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artemfilatov.environmentmonitor.data.repository.MeasurementRepository

class MainViewModelFactory(private val repo: MeasurementRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T
    }
}
