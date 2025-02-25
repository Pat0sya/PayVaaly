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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage


@Composable
fun MyCards(onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {
        // Top Section with Gradient Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF4E54C8), Color(0xFF8F94FB))
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                // Top Bar with Back Arrow and Icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Увеличенная кнопка "Назад"
                    IconButton(
                        onClick = onBackClicked, // Обработчик нажатия
                        modifier = Modifier
                            .size(48.dp) // Увеличиваем размер кнопки
                            .padding(top = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp) // Увеличиваем размер иконки
                        )
                    }
                }

                // Title
                Text(
                    text = "You can check your cards here,",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Cards Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // First Card
                    CardItem(
                        amount = "$4500.00",
                        title = "Company",
                        date = "01/2020",
                        cardNumber = "**** **** **** 2134",
                        logoUrl = "https://storage.googleapis.com/a1aa/image/Km_1arEsxwc-be8YuocK-C1CFdjqloU-ASauC2wwPlA.jpg",
                        gradientColors = listOf(Color(0xFFFF6FD8), Color(0xFF3813C2))
                    )

                    // Second Card
                    CardItem(
                        amount = "$4000.00",
                        title = "Home",
                        date = "01/2020",
                        cardNumber = "**** **** **** 2134",
                        gradientColors = listOf(Color.White, Color.White),
                        textColor = Color.Black
                    )
                }
            }
        }

        // Recent Transactions Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Recent Transaction",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Transaction Items
            TransactionItem(
                iconBackground = Color(0xFFFFC107),
                iconRes = Icons.Default.ShoppingCart,
                title = "Shopping",
                date = "15 March 2021, 8:30 pm",
                amount = "-$120"
            )

            TransactionItem(
                iconBackground = Color(0xFF9C27B0),
                iconRes = Icons.Default.Star,
                title = "Medicine",
                date = "9 March 2021, 10:00 pm",
                amount = "-$89.24"
            )

            TransactionItem(
                iconBackground = Color(0xFF009688),
                iconRes = Icons.Default.Person,
                title = "Sport",
                date = "3 March 2021, 6:57 pm",
                amount = "-$64.85"
            )
        }
    }
}

@Composable
fun CardItem(
    amount: String,
    title: String,
    date: String,
    cardNumber: String,
    logoUrl: String? = null,
    gradientColors: List<Color>,
    textColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .background(
                brush = Brush.horizontalGradient(gradientColors),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = amount,
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = date,
                color = textColor,
                fontSize = 14.sp
            )
            Text(
                text = cardNumber,
                color = textColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            if (logoUrl != null) {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = "Card Logo",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
fun TransactionItem(
    iconBackground: Color,
    iconRes: Any, // Use appropriate type for icons
    title: String,
    date: String,
    amount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconBackground, CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = (iconRes ?: Icons.Default.ShoppingCart) as ImageVector, // iconRes должен быть ImageVector?
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = date,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Text(
            text = amount,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )
    }
}