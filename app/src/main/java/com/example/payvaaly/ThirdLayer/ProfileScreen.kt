package com.example.payvaaly.ThirdLayer

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.payvaaly.R
import com.example.payvaaly.Tools.fetchUserProfileDetails
import com.example.payvaaly.ui.theme.OutlinedButtonBack
import com.example.payvaaly.ui.theme.PrimaryButton
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun ProfileScreen(
    navController: NavController,
    email: String,
    onSave: suspend (currentEmail: String, newFirstName: String, newSecondName: String) -> Boolean
) {
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var secondName by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(email) {
        isLoading = true
        message = ""
        if (email.isNotBlank() && email != "{email}") {
            val userProfile = fetchUserProfileDetails(email)
            if (userProfile != null) {
                firstName = TextFieldValue(userProfile.firstName)
                secondName = TextFieldValue(userProfile.secondName)
            } else {
                message = "Не удалось загрузить данные профиля"
            }
        } else {
            message = "Ошибка: Некорректный email пользователя."
            Log.e("ProfileScreen", "Некорректный email: '$email'")
        }
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Профиль",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Почта: $email",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                StyledInputField(
                    label = "Имя",
                    value = firstName.text,
                    onValueChange = { firstName = TextFieldValue(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                StyledInputField(
                    label = "Фамилия",
                    value = secondName.text,
                    onValueChange = { secondName = TextFieldValue(it) }
                )
            }

            Spacer(modifier = Modifier.height(200.dp))

            PrimaryButton(
                onClick = {
                    if (!isLoading) {
                        message = ""
                        coroutineScope.launch {
                            val success = onSave(email, firstName.text, secondName.text)
                            message = if (success) "Сохранено!" else "Ошибка сохранения"
                        }
                    }
                },
                text = "Сохранить"
            )

            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = message,
                    color = if (message == "Сохранено!") Color(0xFF4CAF50) else Color(0xFFFF5252),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButtonBack(
                onClick = {
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

@Composable
fun StyledInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label, color = Color.White)
        },
        textStyle = TextStyle(color = Color.White),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0x55222222), Color(0x44444444))
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    )
}
