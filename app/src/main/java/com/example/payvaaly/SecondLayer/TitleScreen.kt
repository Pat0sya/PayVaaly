package com.example.payvaaly.SecondLayer

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.payvaaly.Tools.SideBar
import com.example.payvaaly.Tools.fetchBalanceForUser
import com.example.payvaaly.Tools.topUpBalance
import com.example.payvaaly.ui.theme.TopUpButton
import kotlinx.coroutines.launch


@Composable
fun TitleScreen(
    navController: NavController,
    onSignOut: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    email: String // Добавляем email, чтобы получить баланс именно этого пользователя
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // Состояние для хранения баланса пользователя
    var balance by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(email) {
        Log.d("BALANCE_DEBUG", "Email = $email")
        if (email.isNotBlank()) {
            coroutineScope.launch {
                balance = fetchBalanceForUser(email)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { SideBar(navController, onSignOut, isDarkTheme, onToggleTheme,  email) },
        bottomBar = { BottomNavigationBar(
            isDarkTheme = isDarkTheme,
            navController = navController,
            ownerEmail = email
        ) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkTheme) Color.Black else Color(0xFFF3F4F6))
                .padding(paddingValues)
        ) {
            TopSection(
                onMenuClicked = {
                    coroutineScope.launch { scaffoldState.drawerState.open() }
                },
                isDarkTheme = isDarkTheme,
                email = email
            )
            BalanceCard(isDarkTheme, balance)
            var topUpAmount by remember { mutableStateOf("") }
            val context = LocalContext.current

            Column(Modifier.padding(horizontal = 16.dp)) {
                OutlinedTextField(
                    value = topUpAmount,
                    onValueChange = { topUpAmount = it },
                    label = { Text("Сумма пополнения") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                TopUpButton(
                    onClick = {
                        val amount = topUpAmount.toDoubleOrNull()
                        if (amount != null && amount > 0) {
                            coroutineScope.launch {
                                val success = topUpBalance(email, amount)
                                if (success) {
                                    Toast.makeText(context, "Пополнено!", Toast.LENGTH_SHORT).show()
                                    balance = fetchBalanceForUser(email)
                                    topUpAmount = ""
                                } else {
                                    Toast.makeText(context, "Ошибка пополнения", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Введите корректную сумму", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}



@Composable
fun TopSection(onMenuClicked: () -> Unit, isDarkTheme: Boolean, email: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDarkTheme)
                        listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                    else
                        listOf(Color(0xFF3B82F6), Color(0xFF1E40AF))
                ),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenuClicked) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Меню",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center // Центрируем текст
            ) {
                Text(
                    text = "Доброе утро!",
                    color = Color.White,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun BalanceCard(isDarkTheme: Boolean, balance: Double?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = 8.dp,
        backgroundColor = if (isDarkTheme) Color.DarkGray else Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ваш баланс",
                    color = if (isDarkTheme) Color.LightGray else Color.Gray,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = if (isDarkTheme) Color.LightGray else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = if (balance != null) "$balance ₽" else "Загрузка...",
                color = if (balance != null && balance < 0) Color(0xFFF63B3B) else Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}




@Composable
fun BottomNavigationBar(
    isDarkTheme: Boolean,
    navController: NavController,
    ownerEmail: String
) {
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val selectedItemTint = Color(0xFF3B82F6)
    val unselectedItemTint = Color.Gray

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    BottomNavigation(backgroundColor = backgroundColor) {
        // Кошелек (TitleScreen)
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Кошелек",
                    tint = if (currentRoute.startsWith("Title")) selectedItemTint else unselectedItemTint
                )
            },
            selected = currentRoute.startsWith("Title"),
            onClick = {
                navController.navigate("Title?email=$ownerEmail") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                }
            }
        )

        // Контакты (ContactScreen)
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Контакты",
                    tint = if (currentRoute.startsWith("contacts")) selectedItemTint else unselectedItemTint
                )
            },
            selected = currentRoute.startsWith("contacts"),
            onClick = {
                navController.navigate("contacts?ownerEmail=$ownerEmail") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                }
            }
        )

        // Профиль (в будущем)
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Профиль",
                    tint = if (currentRoute == "Profile?email={email}") selectedItemTint else unselectedItemTint
                )
            },
            selected = currentRoute == "Profile?email={email}",
            onClick = {
                navController.navigate("Profile?email={email}") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                }
            }
        )
    }
}


