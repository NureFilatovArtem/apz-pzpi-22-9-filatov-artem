package com.artemfilatov.iotsensors.model

import com.google.gson.annotations.SerializedName

// --- ДОДАЙ ЦЕЙ КЛАС ---
// Для запиту на сервер при логіні
data class LoginRequest(
    val email: String,
    val password: String
)
// --------------------

// Для відповіді від сервера при логіні
data class LoginResponse(
    val token: String,
    val user: User
)

// Модель користувача
data class User(
    val id: Int,
    val username: String,
    val email: String,
    val role: String
)

// Модель сенсора
data class Sensor(
    val id: Int,
    val type: String,
    @SerializedName("building_id") val buildingId: Int?
)

data class Measurement(
    val id: Int,
    @SerializedName("sensor_id") val sensorId: Int,
    val value: Float,
    val unit: String, // <-- ДОДАЙ ЦЕЙ РЯДОК
    val timestamp: String
)

data class Building(
    val id: Int,
    val name: String,
    val address: String?
)

data class Office(
    val id: Int,
    val name: String,
    val location: String?
)