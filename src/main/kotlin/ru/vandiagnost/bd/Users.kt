
//import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Users:  Table("users") {
 val name = varchar("name", 30)
 val email = varchar("email", 30)
 val password = varchar("password", 30)
 val role = varchar("role", 30)
 val createdAt = varchar("createdAt",30)
 val updateAt = varchar("updateAt",30)

    fun insertU(userDTO: UserDTO){

        transaction {
            //val id = Users.insertAndGetId {

            Users.insert{
                //it[id]=userDTO.id
                it[name]=userDTO.name
                it[email]=userDTO.email
                it[password]=userDTO.password
                it[role]=userDTO.role
                it[createdAt]=userDTO.createdAt
                it[updateAt]=userDTO.updateAt
            }
        }
    }
    fun updateUser(name: String, updatedUser: UserDTO) {
        transaction {
            Users.update({ Users.name eq name }) {
                it[Users.name] = updatedUser.name
                it[Users.email] = updatedUser.email
                it[Users.password] = updatedUser.password
                it[Users.role] = updatedUser.role
                it[Users.createdAt] = updatedUser.createdAt
                it[Users.updateAt] = updatedUser.updateAt
            }
        }
    }

    fun fetchUser(name: String): UserDTO?{
        return try{
            transaction {
            val userModel = Users.select {Users.name.eq(name) }.single()

             UserDTO(
                //id= userModel[Users.id].value,
                name= userModel[Users.name],
                email= userModel[Users.email],
                password= userModel[Users.password],
                role= userModel[Users.role],
                createdAt= userModel[Users.createdAt],
                updateAt= userModel[Users.updateAt]
            )
            }
        } catch (e:Exception){
            e.printStackTrace()
           null
        }

    }
    fun deleteUser(name: String): UserDTO? {
        var deletedUser: UserDTO? = null
        transaction {
            val user = Users.select { Users.name eq name }.singleOrNull()
            user?.let {
                deletedUser = UserDTO(
                    it[Users.name],
                    it[Users.email],
                    it[Users.password],
                    it[Users.role],
                    it[Users.createdAt],
                    it[Users.updateAt]
                )
                Users.deleteWhere { Users.name eq name }
            }
        }
        return deletedUser
    }



    fun getAllUsers(): List<UserDTO> {
        return try {
            transaction {
                Users.selectAll().map {
                    //val userModel = Users.select {Users.name.eq(id) }.single()
                    UserDTO(
                        name= it[Users.name],
                        email= it[Users.email],
                        password= it[Users.password],
                        role= it[Users.role],
                        createdAt= it[Users.createdAt],
                        updateAt= it[Users.updateAt]
                    )
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    }

//class cUsers(name: EntityID<String>) : StringEntity(name) {
//    companion object : IntEntityClass<cUsers>(Users)
//    var name  by Users.name
//    var email   by Users.email
//    var password by Users.password
//    var role       by Users.role
//    var createdAt   by Users.createdAt
//    var updateAt       by Users.updateAt
//}

