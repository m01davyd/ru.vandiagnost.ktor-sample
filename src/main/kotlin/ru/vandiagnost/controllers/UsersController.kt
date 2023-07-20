package ru.vandiagnost

import UserDTO
import Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UsersController (private val call: ApplicationCall) {
    suspend fun AllUsers() {
        val userAll = transaction { Users.selectAll().toList() }
        if (userAll.isNotEmpty()) {
            val usersList: MutableList<UserDTO> = mutableListOf()
            userAll.forEach {
                val user = UserDTO(
                    name = it[Users.name],
                    email = it[Users.email],
                    password = it[Users.password],
                    role = it[Users.role],
                    createdAt = it[Users.createdAt],
                    updateAt = it[Users.updateAt]
                )
                usersList.add(user)
            }
            val userList2 = usersList.toList()
            call.respond(userList2)
        } else {
            call.respondText("The \"users\" table is empty", status = HttpStatusCode.NotFound)
        }
    }
    suspend fun updateUser(name: String, updatedUser: UserDTO) {
        val existingUser = Users.fetchUser(name)
        if (existingUser != null) {
            val updatedUserData = updatedUser.copy(
                name = updatedUser.name,
                password = updatedUser.password
            )
            Users.updateUser(name, updatedUserData)
            call.respondText("User updated successfully")
        } else {
            call.respondText("User not found", status = HttpStatusCode.NotFound)
        }
    }

}


