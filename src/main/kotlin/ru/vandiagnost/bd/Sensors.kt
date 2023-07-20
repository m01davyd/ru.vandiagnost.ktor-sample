
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Sensors: IntIdTable("sensors") {
    val name = Sensors.varchar("name", 30)
    val accessCode = Sensors.varchar("accessCode", 30)

        fun insertS(sensorsDTO: SensorsDTO) {

            transaction {
                val id = Sensors.insertAndGetId {
                   //it[id]= sensorsDTO.id
                    it[name] = sensorsDTO.name
                    it[accessCode] = sensorsDTO.accessCode

                }
            }
        }
    fun fetchSensor(name: String): SensorsDTO? {
        return try {
            transaction {

                val sensorModel = Sensors.select { Sensors.name.eq(name) }.single()

                SensorsDTO(
//                    id= sensorModel[Sensors.id].value,
                    name = sensorModel[Sensors.name],
                    accessCode = sensorModel[Sensors.accessCode],

                )

            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun fetchSensorbyid(id: Int): SensorsDTO? {
        return try {
            transaction {

                val sensorModel = Sensors.select { Sensors.id.eq(id) }.single()

                SensorsDTO(
//                    id= sensorModel[Sensors.id].value,
                    name = sensorModel[Sensors.name],
                    accessCode = sensorModel[Sensors.accessCode],

                    )

            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun fetchSensorIdByName(name: String): Pair<SensorsDTO?, Int> {
        return try {
            transaction {
                val sensorModel = Sensors.select { Sensors.name.eq(name) }.singleOrNull()

                if (sensorModel != null) {
                    val sensorsDTO = SensorsDTO(
                        name = sensorModel[Sensors.name],
                        accessCode = sensorModel[Sensors.accessCode]
                    )
                    val sensorId = sensorModel[Sensors.id].value

                    Pair(sensorsDTO, sensorId)
                } else {
                    Pair(null, 0)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(null, 0)
        }
    }


    fun deleteSensors(name: String): SensorsDTO? {
        var deletedSensor: SensorsDTO? = null
        transaction {
            val sen = Sensors.select { Sensors.name eq name }.singleOrNull()
            sen?.let {
                deletedSensor = SensorsDTO(
                    it[Sensors.name],
                    it[Sensors.accessCode]
                )
                Sensors.deleteWhere { Sensors.name eq name }
            }
        }
        return deletedSensor
    }

}