package ru.vandiagnost

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun HTML.index() {
    head {
        title("VanDiagnost")
        link {
            rel = "stylesheet"
            type = "text/css"
            href = "static/styles.css"
        }
        link {
            rel = "shortcut icon"
            href = "static/favicon.png"
            type = "image/png"
        }
    }
    body {
        div {
            id = "root"
            +"React will be here!!"
        }
        script(src = "/static/client1.js") {
        }
    }
}
    fun Application.static() {
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            static("/static") {
                resources()
            }
        }
    }

