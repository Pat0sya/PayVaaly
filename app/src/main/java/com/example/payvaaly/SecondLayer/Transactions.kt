
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TransactionsScreen(onBackClicked: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE) }
    var searchText by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf(loadSearchHistory(sharedPreferences)) }
    var isSearching by remember { mutableStateOf(false) } // Состояние загрузки
    val scope = rememberCoroutineScope()

    val transactions = remember {
        listOf(
            TransactionItemData(Icons.Default.ShoppingCart, "Шопинг", "15 March 2021, 8:30 pm", "-$120"),
            TransactionItemData(Icons.Default.Star, "Медицина", "9 March 2021, 10:00 pm", "-$89.24"),
            TransactionItemData(Icons.Default.Person, "Спорт", "3 March 2021, 6:57 pm", "-$64.85"),
            TransactionItemData(Icons.Default.Clear, "Ресторан", "20 March 2021, 7:15 pm", "-$45.50"),
            TransactionItemData(Icons.Default.Clear, "Такси", "18 March 2021, 5:40 pm", "-$15.75"),
            TransactionItemData(Icons.Default.Home, "Аренда", "1 March 2021, 12:00 pm", "-$800"),
            TransactionItemData(Icons.Default.Clear, "Продукты", "5 March 2021, 2:30 pm", "-$220.10"),
            TransactionItemData(Icons.Default.Clear, "Зарплата", "25 March 2021, 9:00 am", "+$2500"),
            TransactionItemData(Icons.Default.Clear, "Путешествие", "12 March 2021, 11:45 am", "-$540.60"),
            TransactionItemData(Icons.Default.Clear, "Кино", "14 March 2021, 9:00 pm", "-$12.50"),
            TransactionItemData(Icons.Default.Clear, "Мобильная связь", "10 March 2021, 3:20 pm", "-$30"),
            TransactionItemData(Icons.Default.Clear, "Кофе", "22 March 2021, 8:00 am", "-$5.80"),
            TransactionItemData(Icons.Default.Clear, "Фитнес", "17 March 2021, 7:30 am", "-$50"),
            TransactionItemData(Icons.Default.Clear, "Образование", "6 March 2021, 1:00 pm", "-$120.50"),
            TransactionItemData(Icons.Default.Clear,"Интернет", "8 March 2021, 4:00 pm", "-$25")
        )
    }

    val filteredTransactions = remember(searchText) {
        transactions.filter { it.title.contains(searchText, ignoreCase = true) }
    }

    fun updateSearchHistory(newHistory: List<String>) {
        searchHistory = newHistory
    }

    fun onSearch() {
        scope.launch {
            isSearching = true // Показать прогресс
            delay(1000) // Симуляция задержки поиска (заменить на реальную логику)

            if (searchText.isNotEmpty()) {
                addToSearchHistory(searchText, sharedPreferences) { newHistory ->
                    searchHistory = newHistory
                }
            }
            isSearching = false // Скрыть прогресс
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A237E))
    ) {
        // Хедер с общей суммой
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF1976D2),
                    shape = RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp)
                )
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Transactions", color = Color.White)
                        Text(
                            text = "Total: ${calculateTotal(transactions)}",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.size(24.dp))
                }
            }
        }

        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFF0D47A1), CircleShape)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        Icons.Default.Search,
                        "Search",
                        tint = Color.White,
                        modifier = Modifier.clickable { onSearch() }
                    )
                    Spacer(Modifier.width(8.dp))
                    innerTextField()

                    // Отображение прогресса или кнопки очистки
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(23.dp)
                                .padding(end = 3.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else if (searchText.isNotEmpty()) {
                        Icon(
                            Icons.Default.Clear,
                            "Clear",
                            tint = Color.White,
                            modifier = Modifier
                                .clickable { searchText = "" }
                                .padding(end = 8.dp) // Отступ справа, чтобы кнопка не прижималась к краю
                        )
                    }
                }
            }
        )


        Column(Modifier.padding(16.dp)) {
            if (searchHistory.isNotEmpty()) {
                Text(
                    "Очистить историю",
                    color = Color.White,
                    modifier = Modifier
                        .clickable {
                            sharedPreferences.edit().remove("search_history").apply()
                            searchHistory = emptyList()
                        }
                        .padding(8.dp)
                )
                searchHistory.forEach { query ->
                    Text(
                        query,
                        color = Color.White,
                        modifier = Modifier
                            .clickable {
                                searchText = query
                                onSearch()
                            }
                            .padding(8.dp)
                    )
                }
            }
        }

        LazyColumn(Modifier.weight(1f)) {
            items(filteredTransactions) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onClick = {
                        addToSearchHistory(transaction.title, sharedPreferences, ::updateSearchHistory)
                        searchText = transaction.title
                        onSearch()
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionItemData, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(transaction.icon, null, tint = Color.White)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(transaction.title, color = Color.White)
            Text(transaction.date, color = Color.Gray)
        }
        Text(transaction.amount, color = Color.Red)
    }
}

fun addToSearchHistory(
    query: String,
    sharedPreferences: SharedPreferences,
    onHistoryUpdated: (List<String>) -> Unit
) {
    val history = loadSearchHistory(sharedPreferences).toMutableList().apply {
        remove(query)
        add(0, query)
    }
    val newHistory = history.take(10)
    saveSearchHistory(sharedPreferences, newHistory)
    onHistoryUpdated(newHistory)
}

fun loadSearchHistory(sharedPreferences: SharedPreferences): List<String> {
    return sharedPreferences.getStringSet("search_history", emptySet())?.toList() ?: emptyList()
}

fun saveSearchHistory(sharedPreferences: SharedPreferences, history: List<String>) {
    sharedPreferences.edit().putStringSet("search_history", history.toSet()).apply()
}

data class TransactionItemData(
    val icon: ImageVector,
    val title: String,
    val date: String,
    val amount: String
)

fun calculateTotal(transactions: List<TransactionItemData>): String {
    val total = transactions.sumOf {
        val amount = it.amount.replace("[^\\d.-]".toRegex(), "") // Handle only numeric and decimal points
        amount.toDoubleOrNull() ?: 0.0
    }
    return "%.2f".format(total)
}
