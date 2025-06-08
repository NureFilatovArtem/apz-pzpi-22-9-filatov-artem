package com.artemfilatov.iotsensors.api


import retrofit2.Response
import com.artemfilatov.iotsensors.model.*
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // --- ДОДАЙ ЦІ ЕНДПОІНТИ ---
    @GET("api/buildings")
    suspend fun getBuildings(): Response<List<Building>>

    @GET("api/sensors")
    suspend fun getSensors(): Response<List<Sensor>>

    @GET("api/offices")
    suspend fun getOffices(): Response<List<Office>>

    @GET("api/measurements")
    suspend fun getMeasurements(): Response<List<Measurement>>
}