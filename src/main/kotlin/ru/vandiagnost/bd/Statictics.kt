
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction


    object Statictics : IntIdTable("statictics") {
//         val idStatictics = Statictics.integer("idStatictics").autoIncrement()
         val date = Statictics.varchar("date", 30)
         val latitude = Statictics.double("latitude",)
         val longitude = Statictics.double("longitude")
         val temperature = Statictics.double("temperature")
         val batteryCharge = Statictics.integer("batteryCharge")
         val trackCondition = Statictics.varchar("trackCondition", 30)
         val sensorsId = Statictics.integer("sensorsId", )

        fun insertSt(staticticsDTO: StaticticsDTO) {

            transaction {
                val id = Statictics.insertAndGetId {
                    it[date] = staticticsDTO.date
                    it[latitude] = staticticsDTO.latitude
                    it[longitude] = staticticsDTO.longitude
                    it[temperature] = staticticsDTO.temperature
                    it[batteryCharge] = staticticsDTO.batteryCharge
                    it[trackCondition] = staticticsDTO.trackCondition
                    it[sensorsId] = staticticsDTO.sensorsId

                }
            }
        }
        fun fetchStat(sensorsId: Int): StaticticsDTO?{
            return try{
                transaction {

                    val staticModel = Statictics.select { Statictics.sensorsId.eq(sensorsId) }.single()
                   StaticticsDTO(
//                    id= sensorModel[Sensors.id].value,

                        date= staticModel[Statictics.date],
                        latitude= staticModel[Statictics.latitude],
                        longitude= staticModel[Statictics.longitude],
                        temperature= staticModel[Statictics.temperature],
                        batteryCharge= staticModel[Statictics.batteryCharge],
                        trackCondition= staticModel[Statictics.trackCondition],
                        sensorsId=staticModel[Statictics.sensorsId],

                    )


                }
            } catch (e:Exception){
                e.printStackTrace()
              null
            }

        }

        fun deleteStat(sensorsId: Int): StaticticsDTO? {
            var deletedStat: StaticticsDTO? = null
            return try {
                transaction {
                    val stat = Statictics.select { Statictics.sensorsId eq sensorsId }.singleOrNull()
                    stat?.let {
                        deletedStat = StaticticsDTO(
                            it[Statictics.date],
                            it[Statictics.latitude],
                            it[Statictics.longitude],
                            it[Statictics.temperature],
                            it[Statictics.batteryCharge],
                            it[Statictics.trackCondition],
                            it[Statictics.sensorsId]
                        )
                        Statictics.deleteWhere { Statictics.sensorsId eq sensorsId }
                    }
                }
                return deletedStat
            }catch (e:Exception){
                e.printStackTrace()
                null
            }
        }


    }
