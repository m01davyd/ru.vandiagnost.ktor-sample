package ru.vandiagnost.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReceiveRemote(
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val createdAt: String,
    val updateAt: String
)

@Serializable
data class RegisterResponseRemote(
    val token: String
)
