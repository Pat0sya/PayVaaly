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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var searchQuery by remember { mutableStateOf("") }

    val cards = listOf(
        CardData("$4500.00", "Company", "01/2020", "**** **** **** 2134", "https://storage.googleapis.com/a1aa/image/Km_1arEsxwc-be8YuocK-C1CFdjqloU-ASauC2wwPlA.jpg", listOf(Color(0xFFFF6FD8), Color(0xFF3813C2))),
        CardData("$4000.00", "Home", "01/2020", "**** **** **** 2134", null, listOf(Color.White, Color.White), Color.Black),
        CardData("$3250.00", "Savings", "02/2022", "**** **** **** 6789", null, listOf(Color(0xFF008080), Color(0xFF004C4C))),
        CardData("$2900.00", "Business", "06/2021", "**** **** **** 1234", null, listOf(Color(0xFF6A0572), Color(0xFF3A0088))),
        CardData("$1800.00", "Travel", "09/2023", "**** **** **** 5678", null, listOf(Color(0xFFDAA520), Color(0xFFB8860B)))
    )

    val filteredCards = cards.filter { it.title.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {
        // Верхняя панель с градиентом
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color(0xFF4E54C8), Color(0xFF8F94FB))))
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClicked,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White, modifier = Modifier.size(32.dp))
                    }

                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search cards...", color = Color.White.copy(alpha = 0.7f)) },
                        leadingIcon = { Icon(Icons.Default.Search, "Search", tint = Color.White) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }

                Text(
                    text = "You can check your cards here,",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Горизонтальный список карт
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredCards) { card ->
                        CardItem(
                            amount = card.amount,
                            title = card.title,
                            date = card.date,
                            cardNumber = card.cardNumber,
                            logoUrl = card.logoUrl,
                            gradientColors = card.gradientColors,
                            textColor = card.textColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recent Transactions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Recent Transactions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TransactionItem(Color(0xFFFFC107), Icons.Default.ShoppingCart, "Shopping", "15 March 2021, 8:30 pm", "-$120")
            TransactionItem(Color(0xFF9C27B0), Icons.Default.Star, "Medicine", "9 March 2021, 10:00 pm", "-$89.24")
            TransactionItem(Color(0xFF009688), Icons.Default.Person, "Sport", "3 March 2021, 6:57 pm", "-$64.85")
        }
    }
}

data class CardData(
    val amount: String,
    val title: String,
    val date: String,
    val cardNumber: String,
    val logoUrl: String? = null,
    val gradientColors: List<Color>,
    val textColor: Color = Color.White
)

@Composable
fun CardItem(amount: String, title: String, date: String, cardNumber: String, logoUrl: String?, gradientColors: List<Color>, textColor: Color) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(120.dp)
            .background(Brush.horizontalGradient(gradientColors), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(text = amount, color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = title, color = textColor, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = date, color = textColor, fontSize = 14.sp)
            Text(text = cardNumber, color = textColor, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            logoUrl?.let {
                AsyncImage(model = it, contentDescription = "Card Logo", modifier = Modifier.size(30.dp).padding(top = 8.dp), contentScale = ContentScale.Fit)
            }
        }
    }
}

@Composable
fun TransactionItem(iconBackground: Color, iconRes: ImageVector, title: String, date: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(iconBackground).padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(iconRes, null, tint = Color.White)
        }

        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = date, fontSize = 14.sp, color = Color.Gray)
        }

        Text(text = amount, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (amount.startsWith("-")) Color.Red else Color.Green)
    }
}
