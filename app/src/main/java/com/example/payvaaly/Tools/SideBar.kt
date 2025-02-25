package com.example.payvaaly.Tools

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.payvaaly.R

@Composable
fun SideBar(navController: NavController, onSignOut: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {
        // User Profile
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            AsyncImage(
                model = "https://storage.googleapis.com/a1aa/image/cgrdgX5RgZslFcx-O1QQ-t6M00PkDJzYnJ_sL508Un4.jpg",
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Gray, RoundedCornerShape(50))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Emma Watson", fontSize = 18.sp, color = Color.Black)
                Text("@emma_watson", fontSize = 14.sp, color = Color.Gray)
            }
        }

        // Menu Items with custom icons
        SideBarItem(painterResource(id = R.drawable.payment_icon), "Оплаты") {
            navController.navigate("Payment")
        }
        SideBarItem(painterResource(id = R.drawable.transactions_icon), "Транзакции") {
            navController.navigate("Transactions")
        }
        SideBarItem(painterResource(id = R.drawable.cards_icon), "Карты") {
            navController.navigate("MyCards") // Навигация на экран MyCards
        }
        SideBarItem(painterResource(id = R.drawable.promotions_icon), "Акции") {
            navController.navigate("Promotions")
        }
        SideBarItem(painterResource(id = R.drawable.savings_icon), "Сбережения") {
            navController.navigate("Savings")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sign Out Button
        Button(
            onClick = onSignOut,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3B82F6))
        ) {
            Text("Выйти")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sign out")
        }

        // Sign Up Button

    }
}

@Composable
fun SideBarItem(icon: Painter, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Use Painter for custom icons
        Icon(painter = icon, contentDescription = label, tint = Color(0xFF3B82F6))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp, color = Color(0xFF3B82F6))
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Arrow", tint = Color(0xFF3B82F6))
    }
}