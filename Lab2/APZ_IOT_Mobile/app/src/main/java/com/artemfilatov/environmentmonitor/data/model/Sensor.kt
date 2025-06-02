package com.artemfilatov.environmentmonitor.data.model

data class Sensor(
    val id: Int,
    val type: String,
    val officeId: Int? = null
)