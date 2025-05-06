
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.payvaaly.tools.registrationRequest
import com.example.payvaaly.ui.theme.PrimaryButton
import com.example.payvaaly.ui.theme.UnderlineTextField
import kotlinx.coroutines.launch


@Composable

fun SignUpScreen(
    onBackClicked: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var secondName by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.circle),
            contentDescription = null,
            modifier = Modifier
                .size(500.dp)
                .padding(bottom = 150.dp)
                .padding(end = 10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Регистрация",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )

            // Поля ввода
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
            Spacer(modifier = Modifier.height(16.dp))

            UnderlineTextField(
                value = email.text,
                onValueChange = { email = TextFieldValue(it) },
                label = "Почта",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            UnderlineTextField(
                value = password.text,
                onValueChange = { password = TextFieldValue(it) },
                label = "Пароль",
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка регистрации
            PrimaryButton(
                onClick = {
                    coroutineScope.launch {
                        val result = registrationRequest(
                            email = email.text,
                            password = password.text,
                            firstName = firstName.text,
                            secondName = secondName.text
                        )
                        if (result.isSuccess) {
                            message = ""
                            onNavigateToProfile()
                        } else {
                            message = result.exceptionOrNull()?.message ?: "Ошибка"
                        }
                    }
                },
                text = "Создать учетную запись"
            )

            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = message, color = Color.Red)
            }
        }
    }
}
