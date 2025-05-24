package com.example.payvaaly.Tools

import android.util.Log
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
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
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


    @Serializable
    data class TransactionResponse(
        val senderEmail: String,
        val recipientEmail: String,
        val amount: Int,
        val description: String,
        val timestamp: Long
    )


suspend fun fetchTransactions(email: String): List<TransactionResponse> {
    // <--- МЕСТО №1: Проверить значение 'email' ЗДЕСЬ
    Log.d("fetchTransactions", "Вызов fetchTransactions с email: '$email'")

    // Добавим явную проверку ПЕРЕД отправкой запроса:
    if (email.isBlank()) {
        Log.e("fetchTransactions", "ОШИБКА КЛИЕНТА: email пустой ПЕРЕД отправкой запроса на сервер! Запрос не будет выполнен.")
        return emptyList() // Возвращаем пустой список, не делая запрос
    }

    return try {
        val response = client.get("http://10.0.2.2:8080/transactions") {
            parameter("email", email) // <--- 'email' используется здесь для отправки на сервер
        }

        // Если сервер вернул статус, отличный от OK
        if (response.status != HttpStatusCode.OK) {
            // Сюда мы попадаем, если сервер ответил ошибкой.
            // В твоем случае response.status здесь равен HttpStatusCode.BadRequest (400)
            Log.e("fetchTransactions", "Сервер ответил ошибкой: ${response.status}") // Этот лог ты и видишь
            return emptyList() // Возвращаем пустой список
        }

        // Если сервер вернул HttpStatusCode.OK (200)
        val bodyText = response.bodyAsText()
        Log.d("fetchTransactions", "Response body: $bodyText")
        Json.decodeFromString(ListSerializer(TransactionResponse.serializer()), bodyText)

    } catch (e: Exception) { // Если произошла ошибка сети или другая проблема при выполнении запроса
        Log.e("fetchTransactions", "Исключение при выполнении запроса: ${e.message}", e) // Добавил 'e' для полного стектрейса
        emptyList()
    }
}


suspend fun fetchBalanceForUser(email: String): Double? {
    if (email.isBlank()) {
        Log.e("fetchBalanceForUser", "Email is blank, aborting request")
        return null
    }

    return try {
        Log.d("fetchBalanceForUser", "Sending request for balance with email: $email")

        val response = client.get("http://10.0.2.2:8080/balance") {
            parameter("email", email)
        }

        Log.d("fetchBalanceForUser", "Received response with status: ${response.status}")

        if (response.status == HttpStatusCode.OK) {
            val balanceResponse = response.body<BalanceResponse>()
            Log.d("fetchBalanceForUser", "Parsed balance: ${balanceResponse.balance}")
            balanceResponse.balance
        } else {
            val errorBody = response.bodyAsText()
            Log.e("fetchBalanceForUser", "Error response: ${response.status}, body: $errorBody")
            null
        }

    } catch (e: Exception) {
        Log.e("fetchBalanceForUser", "Exception during balance fetch: ${e.message}", e)
        null
    }
}

    @Serializable
    data class BalanceResponse(
        val balance: Double
    )





suspend fun performTransaction(senderEmail: String, recipientEmail: String, amountRubles: Double): Boolean {
    return try {
        val amountInCents = (amountRubles * 100).toInt()
        val transactionDTO = TransactionRequest(
            senderEmail = senderEmail,
            recipientEmail = recipientEmail,
            amount = amountInCents,
            description = "Payment",
            timestamp = System.currentTimeMillis()
        )
        val response = client.post("http://10.0.2.2:8080/transaction") {
            contentType(ContentType.Application.Json)
            setBody(transactionDTO)
        }
        response.status == HttpStatusCode.Created
    } catch (e: Exception) {
        Log.e("Transaction", "Failed to send transaction: ${e.message}")
        false
    }
}


    suspend fun topUpBalance(email: String, amount: Double): Boolean {
        val response = client.post("http://10.0.2.2:8080/topup") {
            contentType(ContentType.Application.Json)
            setBody(TopUpDTO(email, amount))
        }
        return response.status.isSuccess()
    }
    @Serializable
    data class TopUpDTO(
        val email: String,
        val amount: Double
    )
    @Serializable
    data class UserDTO(
        val email: String,
        val firstName: String,
        val secondName: String
    )

    @Serializable
    data class TransactionRequest(
        val senderEmail: String,
        val recipientEmail: String,
        val amount: Int,
        val description: String,
        val timestamp: Long
    )
@Serializable
data class UserProfileDetails(
    val firstName: String,
    val secondName: String

)

suspend fun fetchUserProfileDetails(email: String): UserProfileDetails? {
    if (email.isBlank() || email == "{email}") {
        Log.e("UserProfile", "fetchUserProfileDetails: Некорректный или пустой email: $email")
        return null
    }
    Log.d("UserProfile", "Запрос данных профиля для email: $email")
    return try {
        val response = client.get("http://10.0.2.2:8080/user/profile") { // Обращаемся к новому эндпоинту
            parameter("email", email)
        }
        if (response.status.isSuccess()) {
            response.body<UserProfileDetails>() // Ожидаем UserProfileDetails
        } else {
            Log.e("UserProfile", "Ошибка сервера при загрузке профиля: ${response.status}, тело: ${response.body<String>()}")
            null
        }
    } catch (e: Exception) {
        Log.e("UserProfile", "Исключение при загрузке профиля: ${e.message}", e)
        null
    }
}

// Также на клиенте нужна будет функция для сохранения (для onSave)
suspend fun updateUserProfileOnServer(email: String, firstName: String, secondName: String): Boolean {
    Log.d("UserProfile", "Отправка обновления профиля для $email: $firstName, $secondName")
    return try {
        val requestData = UserProfileUpdateRequest(email, firstName, secondName) // Используем DTO для запроса
        val response = client.post("http://10.0.2.2:8080/user/profile/update") { // Обращаемся к эндпоинту обновления
            contentType(ContentType.Application.Json)
            setBody(requestData)
        }
        if (response.status.isSuccess()) {
            Log.d("UserProfile", "Профиль успешно обновлен на сервере")
            true
        } else {
            Log.e("UserProfile", "Ошибка сервера при обновлении профиля: ${response.status}, тело: ${response.body<String>()}")
            false
        }
    } catch (e: Exception) {
        Log.e("UserProfile", "Исключение при обновлении профиля: ${e.message}", e)
        false
    }
}
@Serializable
data class UserProfileUpdateRequest(
    val email: String, // Чтобы знать, какого пользователя обновлять
    val firstName: String,
    val secondName: String
)