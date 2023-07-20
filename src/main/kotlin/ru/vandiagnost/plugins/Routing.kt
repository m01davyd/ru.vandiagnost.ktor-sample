package ru.vandiagnost.plugins

import UserDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import ru.vandiagnost.UsersController
import ru.vandiagnost.feathures.StatController
import ru.vandiagnost.feathures.login.LoginController
import ru.vandiagnost.feathures.sensors.SensorsController
import ru.vandiagnost.register.RegisterController

@Serializable
data class Test (
    val txt: String
)

fun Route.user() {
        post("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()
        }
        post("/") {
            val loginController = LoginController(call)
            loginController.performLogin()
        }
        get("/sensors") {
            val SensorsController = SensorsController(call)
            SensorsController.AllSensors()
        }
        post("/sensorsAdd") {
            val SensorsController = SensorsController(call)
            SensorsController.registerNewSensors()
        }
        get("/stat") {
            val StatController = StatController(call)
            StatController.AllStat()
        }
        delete("/delete/{sensorsId}") {
            val id = call.parameters["sensorsId"]?.toIntOrNull()
            if (id != null) {
                val statController = StatController(call)
                statController.DeleteSensors(id)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            }
        }
        delete("/delete/d/{sensorsname}") {
            val sensorsname = call.parameters["sensorsname"] ?: return@delete call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            if (sensorsname != null) {
                val senController = SensorsController(call)
                senController.DeleteSensors(sensorsname)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            }
        }
        get("/stat/{name}") {
            val name = call.parameters["name"] ?: return@get
            if (name != null) {
                val stat = StatController(call)
                stat.IdStatictics(name)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            }
        }
    put("/redactor/{username}"){
        val username = call.parameters["username"] ?: return@put
        val updatedUser = call.receive<UserDTO>()
        val usersController = UsersController(call)
        usersController.updateUser(username, updatedUser)
    }
        post("/statAdd") {
            val statController = StatController(call)
            statController.registerNewStat()
        }
         put("/redactor"){
        val user=UsersController(call)
        user.AllUsers()
        }
}


