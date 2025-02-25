package com.example.payvaaly.FirstLayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payvaaly.R
import com.example.payvaaly.ui.theme.OutlinedButton
import com.example.payvaaly.ui.theme.PrimaryButton

@Composable
fun WelcomeScreen(onSignInClicked: () -> Unit, onSignUpClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Локальное изображение из ресурсов
        Image(
            painter = painterResource(id = R.drawable.background), // Укажите ваш ресурс
            contentDescription = null, // Описание для доступности
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Масштабирование изображения
        )

        // "Welcome Back" текст ниже, но не в самом верху
        Text(
            text = "Добро \n \nпожаловать",
            color = Color.White, // Белый цвет текста
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(top = 150.dp) // Отступ сверху, чтобы опустить текст
                .padding(start = 50.dp) // Позиция по центру в верхней части
        )

        // Кнопки внизу экрана
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp).padding(bottom = 50.dp),
            verticalArrangement = Arrangement.Bottom, // Размещаем кнопки внизу
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Кнопка "Sign In"
            PrimaryButton(
                onClick = onSignInClicked,
                text = "Войти"
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Кнопка "Sign Up"
            OutlinedButton(
                onClick = onSignUpClicked,
                text = "Создать учетную запись"
            )
        }
    }
}