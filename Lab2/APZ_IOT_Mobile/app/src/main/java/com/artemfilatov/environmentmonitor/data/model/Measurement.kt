package com.artemfilatov.environmentmonitor.data.model

data class Measurement(
    val id: Int,
    val sensorId: Int,
    val timestamp: String, // ISO формат: "2025-04-08T14:15:00Z"
    val value: Float,
    val unit: String // наприклад: "dB", "lux", "%"
)