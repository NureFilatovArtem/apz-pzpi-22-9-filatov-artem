package com.artemfilatov.environmentmonitor.data.api

import com.artemfilatov.environmentmonitor.data.model.Building
import com.artemfilatov.environmentmonitor.data.model.Measurement
import com.artemfilatov.environmentmonitor.data.model.Office
import com.artemfilatov.environmentmonitor.data.model.Sensor
import com.artemfilatov.environmentmonitor.data.model.Subscription
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("measurements")
    suspend fun getAllMeasurements(): List<Measurement>

    @GET("measurements/history")
    suspend fun getMeasurementHistory(
        @Query("sensorId") sensorId: String
    ): List<Measurement>

    @GET("offices")
    suspend fun getAllOffices(): List<Office>

    @GET("buildings")
    suspend fun getAllBuildings(): List<Building>

    @GET("sensors")
    suspend fun getAllSensors(): List<Sensor>

    @GET("/api/subscriptions")
    suspend fun getAllSubscriptions(): List<Subscription>

}