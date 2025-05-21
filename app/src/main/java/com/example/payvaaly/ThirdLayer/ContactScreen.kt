package com.example.payvaaly.ThirdLayer

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.payvaaly.SecondLayer.BottomNavigationBar
import com.example.payvaaly.Tools.UserDTO
import com.example.payvaaly.Tools.client
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch

@Composable
fun ContactScreen(
    ownerEmail: String,
    navController: NavController,
    isDarkTheme: Boolean
) {
    // Объявляем contacts как список UserDTO (или User, если используешь другой класс)
    var contacts by remember { mutableStateOf<List<UserDTO>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Загрузка контактов с сервера
    suspend fun fetchContacts(email: String): List<UserDTO> {
        return try {
            val response = client.get("http://10.0.2.2:8080/contacts?email=$email")
            if (response.status.isSuccess()) {
                response.body()
            } else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Обновление списка контактов
    fun refreshContacts() {
        coroutineScope.launch {
            contacts = fetchContacts(ownerEmail)
        }
    }

    LaunchedEffect(ownerEmail) {
        refreshContacts()
    }

    // Добавление нового контакта
    suspend fun addContact(contactEmail: String) {
        try {
            val response = client.post("http://10.0.2.2:8080/contacts/add") {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "ownerEmail" to ownerEmail,
                        "contactEmail" to contactEmail
                    )
                )
            }
            if (response.status.isSuccess()) {
                Toast.makeText(context, "Контакт добавлен", Toast.LENGTH_SHORT).show()
                refreshContacts()
            } else {
                Toast.makeText(context, "Не удалось добавить контакт", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка сети", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                isDarkTheme = isDarkTheme,
                navController = navController,
                ownerEmail = ownerEmail
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить контакт")
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (contacts.isEmpty()) {
                    Text(text = "Контакты не найдены", modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn {
                        items(contacts) { user ->
                            Text(text = "${user.firstName} (${user.email})", modifier = Modifier.padding(16.dp))
                            Divider()
                        }
                    }
                }
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Добавить контакт") },
            text = {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Email контакта") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (emailInput.isBlank()) {
                        Toast.makeText(context, "Введите email", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    showDialog = false
                    coroutineScope.launch {
                        addContact(emailInput.trim())
                        emailInput = ""
                    }
                }) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}