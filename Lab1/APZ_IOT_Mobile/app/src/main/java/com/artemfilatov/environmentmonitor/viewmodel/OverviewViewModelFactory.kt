package com.artemfilatov.environmentmonitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artemfilatov.environmentmonitor.data.api.ApiService

class OverviewViewModelFactory(private val api: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OverviewViewModel(api) as T
    }
}
