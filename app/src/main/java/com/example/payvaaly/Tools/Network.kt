package com.example.payvaaly.tools

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

suspend fun loginRequest(email: String, password: String): Result<String> {
    return try {
        val response: HttpResponse = client.post("http://10.0.2.2:8080/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                val loginResponse = response.body<LoginResponse>()
                Result.success(loginResponse.token)
            }
            else -> {
                val error = response.bodyAsText()
                Result.failure(Exception(error))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class LoginResponse(val token: String)
@Serializable
data class RegistrationRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val secondName: String
)

@Serializable
data class RegistrationResponse(
    val token: String
)

suspend fun registrationRequest(
    email: String,
    password: String,
    firstName: String,
    secondName: String
): Result<String> {
    return try {
        val response: HttpResponse = client.post("http://10.0.2.2:8080/registration") {
            contentType(ContentType.Application.Json)
            setBody(RegistrationRequest(email, password, firstName, secondName))
        }

        // Обрабатываем успешный ответ
        if (response.status == HttpStatusCode.OK) {
            val registrationResponse = response.body<RegistrationResponse>()
            Result.success(registrationResponse.token)  // Возвращаем токен
        } else {
            // Если статус не OK, выводим ошибку
            val error = response.bodyAsText()
            Result.failure(Exception(error))
        }
    } catch (e: ClientRequestException) {
        // Обработка ошибки, если пользователь уже существует (409 Conflict)
        if (e.response.status == HttpStatusCode.Conflict) {
            Result.failure(Exception("Пользователь уже существует"))
        } else {
            Result.failure(e)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}