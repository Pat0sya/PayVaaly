package com.example.payvaaly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payvaaly.ui.theme.OutlinedButton
import com.example.payvaaly.ui.theme.PrimaryButton

@Composable
fun WelcomeScreen(onSignInClicked: () -> Unit, onSignUpClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Set background to white
    ) {
        // Light blue circle
        Box(
            modifier = Modifier
                .size(300.dp) // Size of the circle
                .background(
                    color = Color(0xFFADD8E6), // Light blue color
                    shape = CircleShape // Make it a circle
                )
                .align(Alignment.TopEnd) // Position at the top end (top right)
                .offset(x = 100.dp, y = (200).dp) // Adjust position
        )

        // "Welcome Back" text at the top left
        Text(
            text = "Welcome Back",
            color = Color.White, // Black text color
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(16.dp) // Add padding
                .align(Alignment.TopStart) // Position at the top left
        )

        // Buttons and other content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // Add space for the circle
            // Sign In Button (PrimaryButton)
            PrimaryButton(
                onClick = onSignInClicked,
                text = "Sign in"
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Sign Up Button (OutlinedButton)
            OutlinedButton(
                onClick = onSignUpClicked,
                text = "Sign up"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen(onSignInClicked = {}, onSignUpClicked = {})
}