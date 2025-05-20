package com.example.payvaaly.Tools

import android.util.Log
import com.example.payvaaly.SecondLayer.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

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


@Serializable
data class TransactionResponse(
    val id: Int,
    val userEmail: String,
    val amount: Int,
    val description: String,
    val timestamp: Long
)


suspend fun fetchTransactions(email: String): List<TransactionResponse> {
    return try {
        val response = client.get("http://10.0.2.2:8080/transactions") {
            parameter("email", email)
        }

        if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            Log.e("Transactions", "Error: ${response.status}")
            emptyList()
        }
    } catch (e: Exception) {
        Log.e("Transactions", "Exception: ${e.message}")
        emptyList()
    }
}



suspend fun fetchUsersFromApi(): List<User> {
    val response: HttpResponse = client.get("http://10.0.2.2:8080/users")
    return if (response.status == HttpStatusCode.OK) {
        val jsonString = response.bodyAsText()
        Json.decodeFromString(ListSerializer(serializer<User>()), jsonString)
    } else {
        emptyList()
    }
}
suspend fun fetchUserByEmail(email: String): User? {
    return try {
        val response = client.get("http://10.0.2.2:8080/user") {
            parameter("email", email)
        }

        if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            Log.e("fetchUserByEmail", "Error: ${response.status}")
            null
        }
    } catch (e: Exception) {
        Log.e("fetchUserByEmail", "Exception: ${e.message}")
        null
    }
}
suspend fun fetchBalanceForUser(email: String): Double? {
    if (email.isBlank()) return null

    return try {
        val response = client.get("http://10.0.2.2:8080/balance") {
            parameter("email", email)
        }

        if (response.status == HttpStatusCode.OK) {
            val balanceResponse = response.body<BalanceResponse>()
            balanceResponse.balance
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

@Serializable
data class BalanceResponse(
    val balance: Double
)

@Serializable
data class TransactionRequest(
    val recipientEmail: String,
    val amount: Double
)



suspend fun performTransaction(
    recipientEmail: String,
    amount: Double
): Boolean {
    return try {
        val body = TransactionRequest(recipientEmail, amount)
        val response = client.post("http://10.0.2.2:8080/transaction") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        response.status == HttpStatusCode.OK
    } catch (e: Exception) {
        false
    }
}