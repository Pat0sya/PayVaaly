
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payvaaly.R
import com.example.payvaaly.ui.theme.PrimaryButton
import com.example.payvaaly.ui.theme.UnderlineTextField


@Composable
fun SignUpScreen(
    onBackClicked: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.circle), // Укажите ваш ресурс
            contentDescription = null, // Описание для доступности
            modifier = Modifier.size(500.dp).padding(bottom = 150.dp)
                .padding(end = 10.dp)// Отступ сверху, чтобы опустить текст


            // Масштабирование изображения
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Black "Sign up" text
            Text(
                text = "Регистрация",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start) // Align to the start (left)
                    .padding(bottom = 16.dp) // Add spacing below
            )

            // Email TextField with underline
            UnderlineTextField(
                value = email.text,
                onValueChange = { email = TextFieldValue(it) },
                label = "Почта",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField with underline
            UnderlineTextField(
                value = password.text,
                onValueChange = { password = TextFieldValue(it) },
                label = "Пароль",
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Button (PrimaryButton style)
            PrimaryButton(
                onClick = onNavigateToProfile, // Trigger navigation to ProfileCompletionScreen
                text = "Создать учетную запись"
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Back Button (OutlinedButton style)


        }
    }

}