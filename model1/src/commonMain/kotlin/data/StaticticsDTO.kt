


import kotlinx.serialization.Serializable

@Serializable
data class StaticticsDTO (

    val date: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val batteryCharge: Int,
    val trackCondition: String,
    val sensorsId: Int
    )