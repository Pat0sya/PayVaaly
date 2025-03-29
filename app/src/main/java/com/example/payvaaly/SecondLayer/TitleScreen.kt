package com.example.payvaaly.SecondLayer

//noinspection UsingMaterialAndMaterial3Libraries


//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries

//noinspection UsingMaterialAndMaterial3Libraries
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.payvaaly.Tools.SideBar
import kotlinx.coroutines.launch

@Composable

fun TitleScreen(navController: NavController, onSignOut: () -> Unit, isDarkTheme: Boolean, onToggleTheme: () -> Unit) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { SideBar(navController, onSignOut, isDarkTheme, onToggleTheme) },
        bottomBar = { BottomNavigationBar(isDarkTheme = isDarkTheme) }
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
                isDarkTheme = isDarkTheme
            )
            BalanceCard(isDarkTheme)
            CheckBalanceButton(isDarkTheme)
        }
    }
}


@Composable
fun TopSection(onMenuClicked: () -> Unit, isDarkTheme: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDarkTheme) listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                    else listOf(Color(0xFF3B82F6), Color(0xFF1E40AF))
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
                AsyncImage(
                    model = "https://storage.googleapis.com/a1aa/image/cgrdgX5RgZslFcx-O1QQ-t6M00PkDJzYnJ_sL508Un4.jpg",
                    contentDescription = "User profile picture",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Доброе утро,", color = Color.White, fontSize = 18.sp)
            Text(text = "Фиг", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BalanceCard(isDarkTheme: Boolean) {
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
                text = "Требуется взыскание",
                color = Color(0xFFF63B3B),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun CheckBalanceButton(isDarkTheme: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (isDarkTheme) listOf(Color(0xFF444444), Color(0xFF222222))
                    else listOf(Color(0xFF3B82F6), Color(0xFF9333EA))
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Следите за\nвашем балансом",
                color = Color.White,
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}


@Composable
fun BottomNavigationBar(isDarkTheme: Boolean) {
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val selectedItemTint = if (isDarkTheme) Color(0xFF3B82F6) else Color(0xFF3B82F6)  // You can adjust the colors for dark theme if necessary
    val unselectedItemTint = if (isDarkTheme) Color.Gray else Color.Gray

    BottomNavigation(backgroundColor = backgroundColor) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Кошелек",
                    tint = selectedItemTint
                )
            },
            selected = true,
            onClick = {}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = unselectedItemTint
                )
            },
            selected = false,
            onClick = {}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = unselectedItemTint
                )
            },
            selected = false,
            onClick = {}
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewNavigation() {
    TitleScreen(
        navController = rememberNavController(),
        onSignOut = {},
        isDarkTheme = true,
        onToggleTheme = {}
    )
}