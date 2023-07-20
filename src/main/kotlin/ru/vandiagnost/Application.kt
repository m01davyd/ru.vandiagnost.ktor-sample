package ru.vandiagnost

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.vandiagnost.plugins.user

fun main() {
    Database.connect("jdbc:postgresql://localhost:5433/vandiagnost",driver="org.postgresql.Driver",
        "postgres","12345")
    embeddedServer(CIO, port = 8080, host = "127.0.0.1",watchPaths = listOf("classes","resources")){
        main()
    }
        .start(wait = true)
}

fun Application.main() {
    config()
    routing {
        user()
        //userRouting()
        //teachRouting()
    }
    static()
   //configureRouting()
    logRoute()
}
fun Application.config() {
    install(ContentNegotiation) {
        json()
    }
    transaction {
        addLogger(StdOutSqlLogger)
    }
        install(createApplicationPlugin("DelayEmulator") {
            onCall {
                delay(1000L)
            }
        })

}



