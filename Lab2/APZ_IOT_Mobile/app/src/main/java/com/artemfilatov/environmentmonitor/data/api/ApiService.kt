package com.artemfilatov.environmentmonitor.data.api

import com.artemfilatov.environmentmonitor.data.model.Building
import com.artemfilatov.environmentmonitor.data.model.Measurement
import com.artemfilatov.environmentmonitor.data.model.Office
import com.artemfilatov.environmentmonitor.data.model.Sensor
import com.artemfilatov.environmentmonitor.data.model.Subscription
import retrofit2.Response
import retrofit2.http.Body

import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.GET

import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // 🔹 MEASUREMENTS
    @GET("measurements")
    suspend fun getAllMeasurements(): List<Measurement>

    @GET("measurements/history")
    suspend fun getMeasurementHistory(@Query("sensorId") sensorId: String): List<Measurement>


    // 🔹 OFFICES
    @GET("offices")
    suspend fun getAllOffices(): List<Office>

    @POST("offices")
    suspend fun createOffice(@Body office: Office): Response<Office>

    @PUT("offices/{id}")
    suspend fun updateOffice(@Path("id") id: String, @Body office: Office): Response<Office>

    @DELETE("offices/{id}")
    suspend fun deleteOffice(@Path("id") id: String): Response<Unit>


    // 🔹 BUILDINGS
    @GET("buildings")
    suspend fun getAllBuildings(): List<Building>

    @POST("buildings")
    suspend fun createBuilding(@Body building: Building): Response<Building>

    @PUT("buildings/{id}")
    suspend fun updateBuilding(@Path("id") id: String, @Body building: Building): Response<Building>

    @DELETE("buildings/{id}")
    suspend fun deleteBuilding(@Path("id") id: String): Response<Unit>


    // 🔹 SENSORS
    @GET("sensors")
    suspend fun getAllSensors(): List<Sensor>

    @POST("sensors")
    suspend fun createSensor(@Body sensor: Sensor): Response<Sensor>

    @PUT("sensors/{id}")
    suspend fun updateSensor(@Path("id") id: String, @Body sensor: Sensor): Response<Sensor>

    @DELETE("sensors/{id}")
    suspend fun deleteSensor(@Path("id") id: String): Response<Unit>


    // 🔹 SUBSCRIPTIONS
    @GET("subscriptions")
    suspend fun getAllSubscriptions(): List<Subscription>

    @POST("subscriptions")
    suspend fun createSubscription(@Body subscription: Subscription): Response<Subscription>

    @PUT("subscriptions/{id}")
    suspend fun updateSubscription(@Path("id") id: String, @Body subscription: Subscription): Response<Subscription>

    @DELETE("subscriptions/{id}")
    suspend fun deleteSubscription(@Path("id") id: String): Response<Unit>
}