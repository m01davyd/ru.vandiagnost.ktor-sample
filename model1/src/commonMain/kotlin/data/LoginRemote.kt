package ru.vandiagnost.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRecieveRemote(

    val name: String,
    val password: String,
)
@Serializable
data class LoginResponseRemote(
    val token: String
)