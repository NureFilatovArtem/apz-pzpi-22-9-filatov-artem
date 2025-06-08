package com.artemfilatov.environmentmonitor.data.api

import com.artemfilatov.environmentmonitor.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // 🔹 MEASUREMENTS
    @GET("measurements")
    suspend fun getAllMeasurements(): Response<List<Measurement>>

    @GET("measurements/history")
    suspend fun getMeasurementHistory(@Query("sensorId") sensorId: Int): Response<List<Measurement>>

    // 🔹 OFFICES
    @GET("offices")
    suspend fun getAllOffices(): Response<List<Office>>

    @POST("offices")
    suspend fun createOffice(@Body office: Office): Response<Office>

    @PUT("offices/{id}")
    suspend fun updateOffice(@Path("id") id: Int, @Body office: Office): Response<Office>

    @DELETE("offices/{id}")
    suspend fun deleteOffice(@Path("id") id: Int): Response<Unit>

    // 🔹 BUILDINGS
    @GET("buildings")
    suspend fun getAllBuildings(): Response<List<Building>>

    @POST("buildings")
    suspend fun createBuilding(@Body building: Building): Response<Building>

    @PUT("buildings/{id}")
    suspend fun updateBuilding(@Path("id") id: Int, @Body building: Building): Response<Building>

    @DELETE("buildings/{id}")
    suspend fun deleteBuilding(@Path("id") id: Int): Response<Unit>

    // 🔹 SENSORS
    @GET("sensors")
    suspend fun getAllSensors(): Response<List<Sensor>>

    @POST("sensors")
    suspend fun createSensor(@Body sensor: Sensor): Response<Sensor>

    @PUT("sensors/{id}")
    suspend fun updateSensor(@Path("id") id: Int, @Body sensor: Sensor): Response<Sensor>

    @DELETE("sensors/{id}")
    suspend fun deleteSensor(@Path("id") id: Int): Response<Unit>

    // 🔹 SUBSCRIPTIONS
    @GET("subscriptions")
    suspend fun getAllSubscriptions(): Response<List<Subscription>>

    @POST("subscriptions")
    suspend fun createSubscription(@Body subscription: Subscription): Response<Subscription>

    @PUT("subscriptions/{id}")
    suspend fun updateSubscription(@Path("id") id: Int, @Body subscription: Subscription): Response<Subscription>

    @DELETE("subscriptions/{id}")
    suspend fun deleteSubscription(@Path("id") id: Int): Response<Unit>
}
