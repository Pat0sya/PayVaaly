package com.example.payvaaly.ThirdLayer




import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.payvaaly.SecondLayer.BottomNavigationBar
import com.example.payvaaly.Tools.UserDTO
import com.example.payvaaly.Tools.client
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
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
    var contacts by remember { mutableStateOf<List<UserDTO>>(emptyList()) }
    var showAddContactDialog by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf("") }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var contactToDelete by remember { mutableStateOf<UserDTO?>(null) } // Store contact to be deleted

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Log.d("ContactScreen", "ownerEmail: $ownerEmail, isDarkTheme: $isDarkTheme")

    suspend fun fetchContacts(email: String): List<UserDTO> {
        val url = "http://10.0.2.2:8080/contacts"
        Log.d("ContactScreen", "fetchContacts: Запрос на $url с email=$email")
        if (email.isBlank()) {
            Log.e("ContactScreen", "fetchContacts: ownerEmail пустой, запрос не будет выполнен.")
            return emptyList()
        }
        return try {
            val response = client.get(url) { parameter("email", email) }
            Log.d("ContactScreen", "fetchContacts: Статус ответа: ${response.status}")
            if (response.status.isSuccess()) {
                val contactList = response.body<List<UserDTO>>()
                Log.d("ContactScreen", "fetchContacts: Успешно десериализовано ${contactList.size} контактов.")
                contactList
            } else {
                Log.e("ContactScreen", "fetchContacts: Ошибка от сервера, статус: ${response.status}, тело: ${response.body<String>()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ContactScreen", "fetchContacts: Исключение/Ошибка десериализации: ${e.message}", e)
            emptyList()
        }
    }

    fun refreshContacts() {
        coroutineScope.launch {
            contacts = fetchContacts(ownerEmail)
        }
    }

    LaunchedEffect(ownerEmail) {
        refreshContacts()
    }

    suspend fun addContact(contactEmail: String) {
        try {
            val response = client.post("http://10.0.2.2:8080/contacts/add") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("ownerEmail" to ownerEmail, "contactEmail" to contactEmail))
            }
            if (response.status.isSuccess()) {
                Toast.makeText(context, "Контакт добавлен", Toast.LENGTH_SHORT).show()
                refreshContacts()
            } else {
                val errorMsg = response.body<String>()
                Log.e("ContactScreen", "addContact failed: ${response.status}, $errorMsg")
                Toast.makeText(context, "Не удалось добавить контакт: $errorMsg", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("ContactScreen", "addContact exception: ${e.message}", e)
            Toast.makeText(context, "Ошибка сети при добавлении", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun deleteContact(contactUser: UserDTO) {
        try {
            // Assuming your backend expects ownerEmail and the email of the contact to be deleted
            // Adjust the URL and parameters as per your backend API for deleting a contact
            val response = client.delete("http://10.0.2.2:8080/contacts/remove") {
                contentType(ContentType.Application.Json) // Important for sending body with DELETE
                setBody(mapOf("ownerEmail" to ownerEmail, "contactEmail" to contactUser.email))
            }
            if (response.status.isSuccess()) {
                Toast.makeText(context, "Контакт \"${contactUser.firstName} ${contactUser.secondName}\" удален", Toast.LENGTH_SHORT).show()
                refreshContacts()
            } else {
                val errorMsg = response.body<String>()
                Log.e("ContactScreen", "deleteContact failed: ${response.status}, $errorMsg")
                Toast.makeText(context, "Не удалось удалить контакт: $errorMsg", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("ContactScreen", "deleteContact exception: ${e.message}", e)
            Toast.makeText(context, "Ошибка сети при удалении", Toast.LENGTH_SHORT).show()
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
                onClick = { showAddContactDialog = true },
                modifier = Modifier.padding(16.dp),
                backgroundColor = if (isDarkTheme) Color(0xFFBB86FC) else Color(0xFF3B82F6)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить контакт", tint = Color.White)
            }
        },
        backgroundColor = if (isDarkTheme) Color.Black else Color(0xFFF3F4F6)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ContactScreenTopBar(isDarkTheme = isDarkTheme)

            if (contacts.isEmpty()) {
                EmptyContactsView(modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    items(contacts) { user ->
                        ContactRowItem(
                            user = user,
                            isDarkTheme = isDarkTheme,
                            onDeleteClick = {
                                contactToDelete = user // Set the contact to delete
                                showDeleteConfirmDialog = true // Show confirmation dialog
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (showAddContactDialog) {
        AddContactDialog(
            emailInput = emailInput,
            onEmailChange = { emailInput = it },
            onDismiss = { showAddContactDialog = false },
            onConfirm = {
                if (emailInput.isBlank()) {
                    Toast.makeText(context, "Введите email", Toast.LENGTH_SHORT).show()
                } else {
                    showAddContactDialog = false
                    coroutineScope.launch {
                        addContact(emailInput.trim())
                        emailInput = ""
                    }
                }
            }
        )
    }

    if (showDeleteConfirmDialog && contactToDelete != null) {
        DeleteConfirmDialog(
            contactName = "${contactToDelete!!.firstName} ${contactToDelete!!.secondName}",
            onDismiss = {
                showDeleteConfirmDialog = false
                contactToDelete = null
            },
            onConfirm = {
                showDeleteConfirmDialog = false
                coroutineScope.launch {
                    contactToDelete?.let { deleteContact(it) }
                    contactToDelete = null
                }
            }
        )
    }
}

@Composable
fun ContactScreenTopBar(isDarkTheme: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDarkTheme)
                        listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                    else
                        listOf(Color(0xFF3B82F6), Color(0xFF1E40AF))
                )
            )
            .padding(vertical = 20.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Мои Контакты",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ContactRowItem(
    user: UserDTO,
    isDarkTheme: Boolean,
    onDeleteClick: () -> Unit // Callback for delete action
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        backgroundColor = if (isDarkTheme) Color(0xFF2C2C2E) else Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp) // Adjusted padding a bit
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // To push delete icon to the end
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) { // Info takes available space
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Контакт",
                    tint = if (isDarkTheme) Color(0xFFBB86FC) else Color(0xFF3B82F6),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "${user.firstName} ${user.secondName}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = user.email,
                        fontSize = 14.sp,
                        color = if (isDarkTheme) Color.LightGray else Color.Gray
                    )
                }
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить контакт",
                    tint = if (isDarkTheme) Color.LightGray else Color.Gray
                )
            }
        }
    }
}

@Composable
fun EmptyContactsView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Нет контактов",
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Список контактов пуст",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Text(
                text = "Нажмите '+' чтобы добавить новый контакт",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AddContactDialog(
    emailInput: String,
    onEmailChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить новый контакт") },
        text = {
            OutlinedTextField(
                value = emailInput,
                onValueChange = onEmailChange,
                label = { Text("Email контакта") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3B82F6))) {
                Text("Добавить", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFFBB86FC) // это синий цвет
                )
            ) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun DeleteConfirmDialog(
    contactName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подтвердить удаление") },
        text = { Text("Вы уверены, что хотите удалить контакт \"$contactName\"?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFBB86FC)) // Make delete button red
            ) {
                Text("Удалить", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF3B82F6) // это синий цвет
                )
            ) {
                Text("Отмена")
            }
        }
    )
}