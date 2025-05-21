


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payvaaly.Tools.TransactionResponse
import com.example.payvaaly.Tools.fetchTransactions

@Composable
fun TransactionsScreen(
    email: String,
    onBackClicked: () -> Unit
) {
    // Теперь email приходит из параметра, не из SharedPreferences
    // Лог для проверки:
    Log.d("TransactionsScreen", "Полученный email пользователя: '$email'")

    var searchText by remember { mutableStateOf("") }
    val searchHistory = remember { mutableStateListOf<String>() }
    var isLoading by remember { mutableStateOf(false) }
    var transactions by remember { mutableStateOf<List<TransactionItemData>>(emptyList()) }

    LaunchedEffect(email) {
        if (email.isBlank()) {
            Log.w("TransactionsScreen", "Email пользователя пуст. Запрос транзакций не будет выполнен.")
            isLoading = false
            transactions = emptyList()
            return@LaunchedEffect
        }

        isLoading = true
        val fetched: List<TransactionResponse> = fetchTransactions(email)
        transactions = fetched.map { txn ->
            val isOutgoing = txn.senderEmail == email
            val title = if (isOutgoing) {
                "Вы отправили ${formatAmount(txn.amount)} пользователю ${txn.recipientEmail}"
            } else {
                "Вы получили ${formatAmount(txn.amount)} от пользователя ${txn.senderEmail}"
            }
            TransactionItemData(
                icon = Icons.Default.Search, // можно заменить иконку
                title = title,
                date = formatTimestamp(txn.timestamp),
                amount = if (isOutgoing) "-${formatAmount(txn.amount)}" else "+${formatAmount(txn.amount)}"
            )
        }
        isLoading = false
    }


    val filteredTransactions = remember(searchText, transactions) {
        if (searchText.isBlank()) {
            transactions
        } else {
            transactions.filter {
                it.title.contains(searchText, ignoreCase = true) ||
                        it.date.contains(searchText, ignoreCase = true)
            }
        }
    }

    fun addToHistory(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isNotEmpty()) {
            searchHistory.remove(trimmedQuery) // Удаляем, если уже есть, чтобы переместить наверх
            searchHistory.add(0, trimmedQuery) // Добавляем в начало
            if (searchHistory.size > 10) { // Ограничиваем размер истории
                searchHistory.removeLast()
            }
        }
    }

    fun onSearch() {
        if (searchText.isNotBlank()) {
            addToHistory(searchText)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A237E))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Транзакции",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0D47A1), shape = CircleShape)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Поиск",
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                    textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 16.sp),
                    decorationBox = { innerTextField ->
                        if (searchText.isEmpty()) {
                            Text("Поиск транзакций...", color = Color.LightGray.copy(alpha = 0.7f), fontSize = 16.sp)
                        }
                        innerTextField()
                    }
                )
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { searchText = "" }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Очистить поиск",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (searchHistory.isNotEmpty() && searchText.isBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("История поиска", color = Color.White, fontSize = 14.sp)
                Text(
                    "Очистить",
                    color = Color(0xFFF48FB1),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        searchHistory.clear() // Очищаем историю в памяти
                    }
                )
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.height(100.dp)) { // Ограничиваем высоту списка истории
                items(searchHistory) { query ->
                    Text(
                        text = query,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                searchText = query
                            }
                            .padding(vertical = 6.dp)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (filteredTransactions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (email.isBlank()) "Войдите в аккаунт для просмотра транзакций."
                    else "Транзакций не найдено.",
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredTransactions) { transaction ->
                    TransactionItem(transaction = transaction) {
                        // Действие по клику на транзакцию
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionItemData, onClick: () -> Unit) {
    val amountColor = if (transaction.amount.startsWith("+")) Color(0xFF81C784) else Color(0xFFE57373)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(transaction.icon, contentDescription = "Тип транзакции", tint = Color.White)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.title, color = Color.White, fontSize = 16.sp)
            Text(transaction.date, color = Color.LightGray.copy(alpha = 0.7f), fontSize = 12.sp)
        }
        Text(transaction.amount, color = amountColor, fontSize = 16.sp)
    }
}

// Функции loadSearchHistory и saveSearchHistory, работавшие с SharedPreferences, больше не нужны
// если история поиска не сохраняется между сессиями.

data class TransactionItemData(
    val icon: ImageVector,
    val title: String,
    val date: String,
    val amount: String
)

fun formatAmount(amountInCents: Int): String {
    val rubles = amountInCents / 100
    // val cents = amountInCents % 100
    // return String.format("%d.%02d ₽", rubles, cents) // Если нужны копейки
    return "$rubles ₽"
}

fun formatTimestamp(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("dd MMM HH:mm", java.util.Locale("ru")) // Указал русскую локаль
    return format.format(date)
}