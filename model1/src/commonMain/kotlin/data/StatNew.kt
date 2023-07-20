package ru.vandiagnost.feathures

import kotlinx.serialization.Serializable

@Serializable
data class StatNew (

    val date: String,
    val latitude: String,
    val longitude: String,
    val temperature: String,
    val batteryCharge: Int,
    val trackCondition: String,
    val sensorsId: Int
)

@Serializable
data class StatNewResponse(
    val token: String
)