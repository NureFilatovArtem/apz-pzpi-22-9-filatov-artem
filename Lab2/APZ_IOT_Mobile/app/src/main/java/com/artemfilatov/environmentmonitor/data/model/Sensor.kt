package com.artemfilatov.environmentmonitor.data.model

data class Sensor(
    val id: String,
    val type: String,
    val officeId: String? = null // ← nullable
)