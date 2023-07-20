package ru.vandiagnost.feathures.login

import TokenDTO
import Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.vandiagnost.database.users.Tokens
import ru.vandiagnost.login.LoginRecieveRemote
import java.util.*

class LoginController (private val call: ApplicationCall){
    suspend fun performLogin(){
        val receive = call.receive<LoginRecieveRemote>()
        val userDTO = Users.fetchUser(receive.name)//узнали, есть ли такой юзер
        if(userDTO == null){
            call.respond(HttpStatusCode.BadRequest,"User not found")
        } else
            if (userDTO.password == receive.password) {
                val token: String?
                token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(//добавляем токен
                    id=UUID.randomUUID().toString(),
                    name=receive.name,
                    token=token)
                )
                call.respond(HttpStatusCode.OK)
            }
            else{
                call.respond(HttpStatusCode.BadRequest, "Invalid pass")
            }
    }
}

