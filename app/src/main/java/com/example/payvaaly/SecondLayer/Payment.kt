package com.example.payvaaly.SecondLayer



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payvaaly.Tools.client
import com.example.payvaaly.ui.theme.PrimaryButton
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class User(val email: String, val firstName: String, val secondName: String)
@Composable
fun PaymentScreen(
    ownerEmail: String,
    onBackClicked: () -> Unit,
    performTransaction: suspend (recipientEmail: String, amount: Double) -> Boolean
) {
    var amount by remember { mutableStateOf("") }
    var contacts by remember { mutableStateOf<List<User>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    // Функция загрузки контактов с сервера
    suspend fun fetchContacts(): List<User> {
        return try {
            val response = client.get("http://10.0.2.2:8080/contacts?email=$ownerEmail")
            if (response.status.isSuccess()) {
                response.body()
            } else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Загрузка контактов при инициализации
    LaunchedEffect(ownerEmail) {
        contacts = fetchContacts()
        if (contacts.isNotEmpty()) {
            selectedUser = contacts.first()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .padding(16.dp)
    ) {
        // Верхняя панель с кнопкой "назад"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF3B82F6), Color(0xFF1E40AF))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClicked, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Перевод",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(24.dp))
            }
        }

        Spacer(Modifier.height(24.dp))

        // Ввод суммы
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Введите количество", color = Color(0xFF3B82F6), fontSize = 18.sp)
            Text(
                text = "${if (amount.isBlank()) "0" else amount}₽",
                color = Color(0xFF1E40AF),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Выбор получателя из контактов
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Перевод", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))

            Box {
                OutlinedButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = selectedUser?.let { "${it.firstName} ${it.secondName}" } ?: "Выберете получателя",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    contacts.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(text = "${user.firstName} ${user.secondName}") },
                            onClick = {
                                selectedUser = user
                                expanded = false
                            }
                        )
                    }
                    if (contacts.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Нет доступных контактов") },
                            onClick = { }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        PrimaryButton(
            onClick = {
                if (selectedUser != null && amount.toDoubleOrNull() != null && amount.toDouble() > 0) {
                    isLoading = true
                    message = null
                    scope.launch {
                        val success = performTransaction(selectedUser!!.email, amount.toDouble())
                        if (success) {
                            message = "Удачно!"
                            amount = ""
                        } else {
                            message = "Неудачно"
                        }
                        isLoading = false
                    }
                } else {
                    message = "Пожалуйста введите действительное количество"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            text = if (isLoading) "" else "Отправить" // Скрываем текст при загрузке
        )

        Spacer(Modifier.height(12.dp))

        message?.let {
            Text(
                text = it,
                color = if (it.contains("successful")) Color.Green else Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Вызов виртуальной клавиатуры для суммы (пример)
        NumberPad { digit ->
            amount = when (digit) {
                "←" -> if (amount.isNotEmpty()) amount.dropLast(1) else amount
                "." -> if (!amount.contains(".")) "$amount." else amount
                else -> if (amount == "0") digit else amount + digit
            }
        }
    }
}

@Composable
fun NumberPad(onDigitClicked: (String) -> Unit) {
    val digits = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "←")

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        digits.chunked(3).forEach { rowDigits ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowDigits.forEach { digit ->
                    Button(
                        onClick = { onDigitClicked(digit) },
                        modifier = Modifier.weight(1f).padding(4.dp).height(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (digit == "←") Color(0xFF3B82F6) else Color(0xFFE5E7EB),
                            contentColor = if (digit == "←") Color.White else Color.Black
                        )
                    ) {
                        if (digit == "←") {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowLeft,
                                contentDescription = "Delete",
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(text = digit, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
