

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO (
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val createdAt: String,
    val updateAt: String
)




