package com.artemfilatov.environmentmonitor.data.repository

import com.artemfilatov.environmentmonitor.data.api.ApiService
import com.artemfilatov.environmentmonitor.data.model.Subscription

class SubscriptionRepository(private val api: ApiService) {
    suspend fun getAll(): List<Subscription> = api.getAllSubscriptions()
}