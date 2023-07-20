
import kotlinx.serialization.Serializable

@Serializable
data class SensorsNew (
   // val id: Int,
    val name: String,
    val accessCode: String
)

@Serializable
data class SensorsNewResponse(
    val token: String
)