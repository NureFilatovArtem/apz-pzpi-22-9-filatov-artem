package com.artemfilatov.environmentmonitor.data.repository

import com.artemfilatov.environmentmonitor.data.api.ApiService
import com.artemfilatov.environmentmonitor.data.model.Measurement

class MeasurementRepository(private val api: ApiService) {

    suspend fun getLatest(): List<Measurement> {
        return api.getAllMeasurements().takeLast(5)
    }

    suspend fun getHistory(sensorId: String): List<Measurement> {
        return api.getMeasurementHistory(sensorId)
    }
}