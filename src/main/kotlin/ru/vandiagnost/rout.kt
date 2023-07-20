package ru.vandiagnost

import Config
import UserDTO
import Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


fun Route.userRouting() {
    route(Config.usersPath) {


        get {
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
                val userList2=usersList.toList()

                call.respond(userList2)
            } else {
                call.respondText("The \"users\" table is empty", status = HttpStatusCode.NotFound)
            }
        }
    }
}