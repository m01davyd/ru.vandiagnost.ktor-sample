package ru.vandiagnost.database.users

import TokenDTO
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens: Table("tokens") {
    private val id = Tokens.varchar("id",75)
    private val name = Tokens.varchar("name", 30)
    private val token = Tokens.varchar("token", 75)

    fun insert(tokenDTO: TokenDTO) {

        transaction {
            Tokens.insert {
                it[id] = tokenDTO.id
                it[name] = tokenDTO.name
                it[token] = tokenDTO.token

            }
        }
    }
    fun fetchTokens(): List<TokenDTO> {
        return try {
            transaction {
                Tokens.selectAll().toList()
                    .map {
                        TokenDTO(
                            id = it[Tokens.id],
                            token = it[Tokens.token],
                            name = it[Tokens.name]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}



