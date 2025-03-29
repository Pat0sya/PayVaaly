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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

data class CardData(
    val amount: String,
    val title: String,
    val date: String,
    val cardNumber: String,
    val imageUrl: String?
)

@Composable
fun MyCards(onBackClicked: () -> Unit) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var cards by remember { mutableStateOf<List<CardData>>(emptyList()) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        val newCards = listOf(
            CardData(
                amount = "$4500.00",
                title = "Компания",
                date = "01/2020",
                cardNumber = "**** **** **** 2134",
                imageUrl = fetchRandomImageUrl()
            ),
            CardData(
                amount = "$4000.00",
                title = "Дом",
                date = "01/2020",
                cardNumber = "**** **** **** 2134",
                imageUrl = fetchRandomImageUrl()
            ),
            CardData(
                amount = "$10.00",
                title = "Cemья",
                date = "01/2020",
                cardNumber = "**** **** **** 2134",
                imageUrl = fetchRandomImageUrl()
            ),
            CardData(
                amount = "$0.00",
                title = "Запаска",
                date = "01/2020",
                cardNumber = "**** **** **** 2134",
                imageUrl = fetchRandomImageUrl()
            ),
        )
        cards = newCards
    }

    val filteredCards = cards.filter { it.title.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6))
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(Color(0xFF4E54C8))
                .padding(16.dp)
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
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search cards...", color = Color.Gray) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        searchQuery = "" // Очищаем поле поиска
                                        focusManager.clearFocus() // Убираем фокус с поля ввода
                                    }
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Следите за своими картами",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredCards) { card ->
                CardItem(card)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        RecentTransactionsSection()
    }
}

@Composable
fun CardItem(card: CardData) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
    ) {
        AsyncImage(
            model = card.imageUrl,
            contentDescription = "Card Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = card.amount, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = card.title, color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun RecentTransactionsSection() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Недавние транзакции",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val transactions = listOf(
            TransactionItemData(Icons.Default.ShoppingCart, "Шопинг", "15 March 2021, 8:30 pm", "-$120"),
            TransactionItemData(Icons.Default.Star, "Медецина", "9 March 2021, 10:00 pm", "-$89.24"),
            TransactionItemData(Icons.Default.Person, "Спорт", "3 March 2021, 6:57 pm", "-$64.85")
        )

        transactions.forEach { transaction ->
            TransactionItem(transaction)
        }
    }
}

data class TransactionItemData(
    val icon: ImageVector,
    val title: String,
    val date: String,
    val amount: String
)

@Composable
fun TransactionItem(transaction: TransactionItemData) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = transaction.icon, contentDescription = null, tint = Color.White)
        }

        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
        ) {
            Text(text = transaction.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = transaction.date, fontSize = 14.sp, color = Color.Gray)
        }

        Text(
            text = transaction.amount,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (transaction.amount.startsWith("-")) Color.Red else Color.Green
        )
    }
}

suspend fun fetchRandomImageUrl(): String? {
    return withContext(Dispatchers.IO) {
        try {
            val accessKey = "y_rM7H24hLO31uB5UdxyWXYshXsFG9gt8c32ItYP_pI"
            val url = "https://api.unsplash.com/photos/random?client_id=$accessKey"
            val json = URL(url).readText()
            println("API Response: $json") // Логируем ответ
            JSONObject(json).getJSONObject("urls").getString("regular")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}