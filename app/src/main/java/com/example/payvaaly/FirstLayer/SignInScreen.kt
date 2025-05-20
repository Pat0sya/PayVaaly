package com.example.payvaaly.FirstLayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payvaaly.R
import com.example.payvaaly.Tools.loginRequest
import com.example.payvaaly.ui.theme.OutlinedButtonBack
import com.example.payvaaly.ui.theme.PrimaryButton
import com.example.payvaaly.ui.theme.UnderlineTextField
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    onBackClicked: () -> Unit,
    onSignInSuccess: (String) -> Unit  // Теперь передаем email при успешном входе
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.circle),
            contentDescription = null,
            modifier = Modifier
                .size(500.dp)
                .padding(bottom = 150.dp)
                .padding(end = 10.dp)
        )
        Text(
            text = "Добро \n \nпожаловать",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(top = 120.dp)
                .padding(start = 30.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp, top = 150.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Вход",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )

            UnderlineTextField(
                value = email,
                onValueChange = { email = it },
                label = "Почта",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            UnderlineTextField(
                value = password,
                onValueChange = { password = it },
                label = "Пароль",
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                onClick = {
                    coroutineScope.launch {
                        val result = loginRequest(email, password)
                        result.onSuccess {
                            onSignInSuccess(email)  // передаем email обратно при успехе
                        }.onFailure {
                            errorMessage = it.message
                        }
                    }
                },
                text = "Войти"
            )
            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButtonBack(
                onClick = onBackClicked,
                text = "Назад",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


