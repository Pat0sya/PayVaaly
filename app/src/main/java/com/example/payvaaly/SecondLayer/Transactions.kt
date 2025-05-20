
import android.content.Context
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payvaaly.Tools.fetchTransactions

@Composable
fun TransactionsScreen(onBackClicked: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE) }

    var searchText by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf(loadSearchHistory(sharedPreferences)) }
    var isLoading by remember { mutableStateOf(false) }
    var transactions by remember { mutableStateOf<List<TransactionItemData>>(emptyList()) }

    val coroutineScope = rememberCoroutineScope()

    // Получаем email из SharedPreferences (или другого источника)
    val email = sharedPreferences.getString("user_email", "") ?: ""

    // Загрузка транзакций при старте и при изменении email
    LaunchedEffect(email) {
        isLoading = true
        val fetched = fetchTransactions(email)
        transactions = fetched.map {
            TransactionItemData(
                icon = Icons.Default.Search,
                title = it.description,
                date = formatTimestamp(it.timestamp),
                amount = formatAmount(it.amount)
            )
        }
        isLoading = false
    }

    // Фильтрация транзакций по поисковому запросу
    val filteredTransactions = remember(searchText, transactions) {
        if (searchText.isBlank()) transactions
        else transactions.filter {
            it.title.contains(searchText, ignoreCase = true)
        }
    }

    // Обновление истории поиска
    fun addToHistory(query: String) {
        val newHistory = (listOf(query) + searchHistory.filter { it != query }).take(10)
        saveSearchHistory(sharedPreferences, newHistory)
        searchHistory = newHistory
    }

    // Обработка поиска (просто фильтрация уже есть, можно здесь сохранять историю)
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
        // Верхняя панель с кнопкой назад и заголовком
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Транзакции",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(8.dp))

        // Поисковая строка
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0D47A1), shape = CircleShape)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.clickable { onSearch() }
                )
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { searchText = "" }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // История поиска
        if (searchHistory.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("История поиска", color = Color.White, fontSize = 14.sp)
                Text(
                    "Очистить",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        saveSearchHistory(sharedPreferences, emptyList())
                        searchHistory = emptyList()
                    }
                )
            }
            Spacer(Modifier.height(4.dp))
            Column {
                searchHistory.forEach { query ->
                    Text(
                        text = query,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                searchText = query
                                onSearch()
                            }
                            .padding(vertical = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // Список транзакций
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
                Text("Транзакций не найдено", color = Color.White)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredTransactions) { transaction ->
                    TransactionItem(transaction = transaction) {
                        // При клике — добавить в историю поиска и отфильтровать
                        searchText = transaction.title
                        onSearch()
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionItemData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(transaction.icon, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.title, color = Color.White, fontSize = 16.sp)
            Text(transaction.date, color = Color.LightGray, fontSize = 12.sp)
        }
        Text(transaction.amount, color = Color.Red, fontSize = 16.sp)
    }
}

fun loadSearchHistory(sharedPreferences: android.content.SharedPreferences): List<String> {
    return sharedPreferences.getStringSet("search_history", emptySet())?.toList() ?: emptyList()
}

fun saveSearchHistory(sharedPreferences: android.content.SharedPreferences, history: List<String>) {
    sharedPreferences.edit().putStringSet("search_history", history.toSet()).apply()
}

data class TransactionItemData(
    val icon: ImageVector,
    val title: String,
    val date: String,
    val amount: String
)

fun formatAmount(amount: Int): String {
    return if (amount >= 0) "+$amount ₽" else "-${-amount} ₽"
}

fun formatTimestamp(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault())
    return format.format(date)
}