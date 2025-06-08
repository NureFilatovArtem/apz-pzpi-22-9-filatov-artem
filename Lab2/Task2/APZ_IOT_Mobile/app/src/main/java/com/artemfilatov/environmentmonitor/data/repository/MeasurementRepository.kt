package com.artemfilatov.environmentmonitor.data.repository

import com.artemfilatov.environmentmonitor.data.api.ApiService
import com.artemfilatov.environmentmonitor.data.model.Measurement
import retrofit2.Response
import java.io.IOException

class MeasurementRepository(private val api: ApiService) {

    suspend fun getLatest(): List<Measurement> {
        return handleResponse(api.getAllMeasurements()) { response ->
            response.takeLast(5)
        }
    }

    suspend fun getHistory(sensorId: Int): List<Measurement> {
        return handleResponse(api.getMeasurementHistory(sensorId))
    }

    private suspend fun <T> handleResponse(
        response: Response<T>,
        transform: (T) -> T = { it }
    ): T {
        if (response.isSuccessful) {
            response.body()?.let { body ->
                return transform(body)
            } ?: throw IOException("Empty response body")
        } else {
            throw IOException("API call failed with code: ${response.code()}")
        }
    }
}