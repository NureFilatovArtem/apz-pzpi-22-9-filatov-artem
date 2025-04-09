package com.artemfilatov.environmentmonitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artemfilatov.environmentmonitor.data.api.ApiService
import com.artemfilatov.environmentmonitor.ui.entity.EntityViewModel

class EntityViewModelFactory(private val api: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EntityViewModel(api) as T
    }
}
