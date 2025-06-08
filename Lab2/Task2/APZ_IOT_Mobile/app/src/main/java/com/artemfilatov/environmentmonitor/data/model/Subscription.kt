package com.artemfilatov.environmentmonitor.data.model

data class Subscription(
    val sensor_id: Int,
    val callback_url: String,
    val created_at: String, // можеш використати LocalDateTime, якщо парсиш
    val updated_at: String
)