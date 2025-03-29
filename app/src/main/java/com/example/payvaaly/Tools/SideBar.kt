package com.example.payvaaly.Tools


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.payvaaly.R

@Composable
fun SideBar(navController: NavController, onSignOut: () -> Unit, isDarkTheme: Boolean, onToggleTheme: () -> Unit) {
    Column(

        modifier = Modifier
            .fillMaxHeight()
            .width(360.dp)
            .background(if (isDarkTheme) Color.Black else Color.White)
            .padding(16.dp)
    ) {
        //Spacer(modifier = Modifier.width(100.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            AsyncImage(
                model = "https://storage.googleapis.com/a1aa/image/cgrdgX5RgZslFcx-O1QQ-t6M00PkDJzYnJ_sL508Un4.jpg",
                contentDescription = "Profile Picture",

            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Emma Watson", fontSize = 18.sp, color = if (isDarkTheme) Color.White else Color.Black)
                Text("@emma_watson", fontSize = 14.sp, color = Color.Gray)
            }
        }

        SideBarItem(painterResource(id = R.drawable.payment_icon), "Оплаты", isDarkTheme = isDarkTheme) {
            navController.navigate("Payment")
        }
        SideBarItem(painterResource(id = R.drawable.transactions_icon), "Транзакции",isDarkTheme = isDarkTheme) {
            navController.navigate("Transactions")
        }
        SideBarItem(painterResource(id = R.drawable.cards_icon), "Карты", isDarkTheme = isDarkTheme ){
            navController.navigate("MyCards")
        }
        SideBarItem(painterResource(id = R.drawable.promotions_icon), "Акции", isDarkTheme = isDarkTheme) {
            navController.navigate("Promotions")
        }
        SideBarItem(painterResource(id = R.drawable.savings_icon), "Сбережения", isDarkTheme = isDarkTheme) {
            navController.navigate("Savings")
        }


        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onToggleTheme,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3B82F6))
        ) {
            Icon(
                painter = painterResource(id = if (isDarkTheme) R.drawable.baseline_brightness_4_24 else R.drawable.baseline_brightness_7_24),
                contentDescription = "Toggle Theme"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isDarkTheme) "Светлая тема" else "Темная тема", )
        }

        Button(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3B82F6))
        ) {
            Text("Выйти")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sign out", )
        }
    }
}

@Composable
fun SideBarItem(icon: Painter, label: String, isDarkTheme: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = icon, contentDescription = label, tint = if (isDarkTheme) Color.LightGray else Color(0xFF3B82F6))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp, color = if (isDarkTheme) Color.White else Color(0xFF3B82F6))
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Arrow", tint = if (isDarkTheme) Color.LightGray else Color(0xFF3B82F6))
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewSideBar() {
    // Заглушка для NavController
    val navController = rememberNavController()

    // Заглушка для onSignOut и onToggleTheme
    SideBar(
        navController = navController,
        onSignOut = { },
        isDarkTheme = true, // Для светлой темы
        onToggleTheme = { }
    )
}