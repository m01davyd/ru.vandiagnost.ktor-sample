package ru.vandiagnost.feathures.sensors
import Sensors
import SensorsDTO
import SensorsNew
import SensorsNewResponse
import TokenDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.vandiagnost.database.users.Tokens
import java.util.*
class SensorsController(private val call: ApplicationCall) {
    suspend fun registerNewSensors() { //получили запрос от сервера
        val sensorsNew = call.receive<SensorsNew>()
        val senDTO = Sensors.fetchSensor(sensorsNew.name)//узнали, есть ли такой датчик
        if (senDTO != null) {//если сущетсвует, то
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString() //генерация токена
            try {
                Sensors.insertS( //добавляем датчик в бд
                    SensorsDTO(
                        name = sensorsNew.name,
                        accessCode = sensorsNew.accessCode
                        )
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, "Cant  ${e.localizedMessage}")
            }
            Tokens.insert(
                TokenDTO(//добавляем токен
                    id = UUID.randomUUID().toString(),
                    name = sensorsNew.name,
                    token = token
                )
            )
            call.respond(SensorsNewResponse(token = token))
        }
    }
    suspend fun AllSensors() {
        val sensorsAll = transaction { Sensors.selectAll().toList() }
        if (sensorsAll.isNotEmpty()) {
            val sensorsList: MutableList<SensorsDTO> = mutableListOf()
            sensorsAll.forEach {
                val sensor = SensorsDTO(
                    name = it[Sensors.name],
                    accessCode = it[Sensors.accessCode],
                    )
                sensorsList.add(sensor)
            }
            val sensorsList2 = sensorsList.toList()
            call.respond(sensorsList2)
        } else {
            call.respondText("The \"sensors\" table is empty", status = HttpStatusCode.NotFound)
        }
    }
    suspend fun DeleteSensors(name: String) {
        val id=Sensors.fetchSensorIdByName(name)//найти айди датчика по имени
        val idid=id.second//айди датчика
        val deletedStat = Sensors.deleteSensors(name)//удалить датчик по имени
        if (deletedStat != null) {
            call.respond(HttpStatusCode.OK, "Sensor deleted: $deletedStat")
        } else {
            call.respond(HttpStatusCode.NotFound, "Sensor not found")
        }
    }
}

