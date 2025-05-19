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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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
import kotlinx.serialization.Serializable

// DTO для пользователя (просто пример)
@Serializable
data class User(val email: String, val firstName: String, val secondName: String)

@Composable
fun Payment(
    onBackClicked: () -> Unit,
    fetchUsers: suspend () -> List<User>  // функция загрузки пользователей с сервера
) {
    var amount by remember { mutableStateOf("0") }
    var users by remember { mutableStateOf(emptyList<User>()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    val scope = rememberCoroutineScope()

    // Загружаем пользователей при запуске
    LaunchedEffect(Unit) {
        users = fetchUsers()
        if (users.isNotEmpty()) {
            selectedUser = users.first()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .padding(16.dp)
    ) {
        // Top Bar
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
                IconButton(
                    onClick = onBackClicked,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Transfer",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(24.dp))
            }
        }

        // Amount Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter amount",
                color = Color(0xFF3B82F6),
                fontSize = 18.sp
            )
            Text(
                text = "$${amount}",
                color = Color(0xFF1E40AF),
                fontSize = 48.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Recipient Dropdown
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "To",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Box {
                // Кнопка выбора пользователя
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (selectedUser != null) {

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${selectedUser!!.firstName} ${selectedUser!!.secondName}",
                            color = Color.Black,
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Text("Select recipient")
                    }
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowForward else Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    users.forEach { user ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {

                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "${user.firstName} ${user.secondName}")
                                }
                            },
                            onClick = {
                                selectedUser = user
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // Number Pad
        NumberPad { digit ->
            amount = if (amount == "0") digit else amount + digit
        }
    }
}


@Composable
fun NumberPad(onDigitClicked: (String) -> Unit) {
    val digits = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "→")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        digits.chunked(3).forEach { rowDigits ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowDigits.forEach { digit ->
                    Button(
                        onClick = { onDigitClicked(digit) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .height(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (digit == "→") Color(0xFF3B82F6) else Color(0xFFE5E7EB),
                            contentColor = if (digit == "→") Color.White else Color.Black
                        )
                    ) {
                        if (digit == "→") {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Send",
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = digit,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}