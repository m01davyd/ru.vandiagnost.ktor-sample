package ru.vandiagnost.register

import TokenDTO
import UserDTO
import Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.vandiagnost.database.users.Tokens
import java.util.*

class RegisterController(private val call: ApplicationCall) {
    suspend fun registerNewUser(){ //получили запрос от сервера
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
             val userDTO = Users.fetchUser(registerReceiveRemote.name)//узнали, есть ли такой юзер
        if (userDTO!=null) {//если сущетсвует, то
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else{//если нет
            val token = UUID.randomUUID().toString() //генерация токена
            try {
                Users.insertU( //добавляем пользователя в бд
                    UserDTO(
                       //id= registerReceiveRemote.id,
                        name = registerReceiveRemote.name,
                        email = registerReceiveRemote.email,
                        password = registerReceiveRemote.password,
                        role = registerReceiveRemote.role,
                        createdAt = registerReceiveRemote.createdAt,
                        updateAt = registerReceiveRemote.updateAt
                    ) )  }
                catch (e: ExposedSQLException){//debug
                    call.respond(HttpStatusCode.Conflict, "User already exists")
                }
            catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, "Cant  ${e.localizedMessage}")
            }
            Tokens.insert(TokenDTO(//добавляем токен
                id=UUID.randomUUID().toString(),
                name=registerReceiveRemote.name,
                token=token))
            call.respond(RegisterResponseRemote(token=token))
        }
    }
}