package ru.vandiagnost.feathures

import Sensors
import Statictics
import StaticticsDTO
import TokenDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.vandiagnost.database.users.Tokens
import java.util.*

class StatController(private val call: ApplicationCall) {
    suspend fun registerNewStat() { //получили запрос от сервера
        val statNew = call.receive<StatNew>()
        val statDTO = Statictics.fetchStat(statNew.sensorsId)//узнали, есть ли такой датчик
        if (statDTO != null) {//если сущетсвует, то
            call.respond(HttpStatusCode.Conflict, "Stat already exists")
        } else {
            val token = UUID.randomUUID().toString() //генерация токена
            try {
                Statictics.insertSt( //добавляем пользователя в бд
                    StaticticsDTO(
                        date=statNew.date,
                        longitude = statNew.longitude.toDouble(),
                        latitude = statNew.latitude.toDouble(),
                        temperature=statNew.temperature.toDouble(),
                        batteryCharge = statNew.batteryCharge,
                        trackCondition=statNew.trackCondition,
                        sensorsId = statNew.sensorsId,

                    )
                )

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, "Cant  ${e.localizedMessage}")
            }
            Tokens.insert(
                TokenDTO(//добавляем токен
                    id = UUID.randomUUID().toString(),
                    name = statNew.sensorsId.toString(),
                    token = token
                )
            )
            call.respond(StatNewResponse(token = token))
        }

    }

    suspend fun AllStat() {
        val statAll = transaction { Statictics.selectAll().toList() }
        if (statAll.isNotEmpty()) {
            val statistList: MutableList<StaticticsDTO> = mutableListOf()
            statAll.forEach {
                val stat = StaticticsDTO(
                    //id= it[Sensors.id].value,
                    date = it[Statictics.date],
                    latitude = it[Statictics.latitude],
                    longitude = it[Statictics.longitude],
                    temperature = it[Statictics.temperature],
                    batteryCharge = it[Statictics.batteryCharge],
                    trackCondition = it[Statictics.trackCondition],
                    sensorsId = it[Statictics.sensorsId],
                )

                statistList.add(stat)
            }
            val statistList2 = statistList.toList()
            call.respond(statistList2)
        } else {
            call.respondText("The \"statictics\" table is empty", status = HttpStatusCode.NotFound)
        }
    }
    suspend fun DeleteSensors(Id: Int) {
        val deletedStat = Statictics.deleteStat(Id)
        if (deletedStat != null) {
            call.respond(HttpStatusCode.OK, "Sensor deleted: $deletedStat")
        } else {
            call.respond(HttpStatusCode.NotFound, "Sensor not found")
        }
    }
    suspend fun IdStatictics(name:String){
        val sensors= Sensors.fetchSensorIdByName(name)
        val sensorDTO=sensors.first
        val sensorId=sensors.second
        if (sensorDTO != null ) {
            val statDTO = Statictics.fetchStat(sensorId)
            val statistList: MutableList<StaticticsDTO?> = mutableListOf()
            statistList.add(statDTO)
            val statistList2 = statistList.toList()

            if (statDTO != null) {
                call.respond(statistList2)
            } else {
                call.respond(HttpStatusCode.NotFound, "Statistics not found for sensor with name: $name")
            }
        } else {
            call.respond(HttpStatusCode.NotFound, "Sensor not found with name: $name")
        }
    }




    }
