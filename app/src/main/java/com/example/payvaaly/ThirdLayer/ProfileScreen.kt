package com.example.payvaaly.ThirdLayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.payvaaly.R
import com.example.payvaaly.ui.theme.OutlinedButtonBack
import com.example.payvaaly.ui.theme.PrimaryButton
import com.example.payvaaly.ui.theme.UnderlineTextField
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun ProfileScreen(
    navController: NavController,
    email: String,
    firstNameInit: String,
    secondNameInit: String,
    onSave: suspend (String, String) -> Boolean,
    onBackClicked: () -> Boolean,
) {
    var firstName by remember { mutableStateOf(TextFieldValue(firstNameInit)) }
    var secondName by remember { mutableStateOf(TextFieldValue(secondNameInit)) }
    var message by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Профиль",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Почта: $email",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(24.dp))

            UnderlineTextField(
                value = firstName.text,
                onValueChange = { firstName = TextFieldValue(it) },
                label = "Имя",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            UnderlineTextField(
                value = secondName.text,
                onValueChange = { secondName = TextFieldValue(it) },
                label = "Фамилия",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                onClick = {
                    coroutineScope.launch {
                        val success = onSave(firstName.text, secondName.text)
                        message = if (success) "Сохранено!" else "Ошибка сохранения"
                    }
                },
                text = "Сохранить"
            )

            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = message,
                    color = if (message == "Сохранено!") Color.Green else Color.Red
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButtonBack(
                onClick = {
                    // Кодируем email, чтобы избежать проблем с URL
                    val encodedEmail = URLEncoder.encode(email, "UTF-8")
                    navController.navigate("Title?email=$encodedEmail") {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                },
                text = "Назад",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}