package com.example.payvaaly



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payvaaly.ui.theme.PrimaryButton
import com.example.payvaaly.ui.theme.OutlinedButton
import com.example.payvaaly.ui.theme.UnderlineTextField

@Composable
fun SignUpScreen(
    onBackClicked: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Black "Sign up" text
        Text(
            text = "Sign up",
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
            label = "Email",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField with underline
        UnderlineTextField(
            value = password.text,
            onValueChange = { password = TextFieldValue(it) },
            label = "Password",
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Button (PrimaryButton style)
        PrimaryButton(
            onClick = onNavigateToProfile, // Trigger navigation to ProfileCompletionScreen
            text = "Sign up"
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Back Button (OutlinedButton style)
        OutlinedButton(
            onClick = onBackClicked,
            text = "Back"
        )
    }
}