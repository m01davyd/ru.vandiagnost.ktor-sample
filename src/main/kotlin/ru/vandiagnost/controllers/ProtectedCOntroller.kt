//package ru.vandiagnost.controllers
//
//import io.ktor.http.*
//import io.ktor.server.application.*
//import io.ktor.server.response.*
//import ru.vandiagnost.database.users.Tokens
//
//class ProtectedController(private val call: ApplicationCall) {
//    suspend fun handleRequest() {
//        val token = call.request.headers["Authorization"] // Получение токена из заголовка запроса
//
//        if (token == null) {
//            call.respond(HttpStatusCode.Unauthorized, "Missing token")
//            return
//        }
//
//        // Проверка токена в базе данных или другом хранилище
//        val isValidToken = Tokens.fetchToken(token)
//
//        if (isValidToken) {
//            // Токен верен, пользователь аутентифицирован
//            // Обработка запроса для защищенного ресурса
//            call.respond("Protected resource")
//        } else {
//            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
//        }
//    }