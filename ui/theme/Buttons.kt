// Buttons.kt
package com.example.payvaaly.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Light blue color for the outline
val LightBlue = Color(0xFF455EE5)

// Gradient colors
val GradientColors = listOf(Color(0xFFB56EE7), Color(0xFF3B82F6)) // Green to blue gradient

// Default button style (gradient background, white text)
@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(60.dp) // Fixed height
            .widthIn(min = 20.dp) // Minimum width
            .background(
                brush = Brush.horizontalGradient(colors = GradientColors), // Gradient background
                shape = RoundedCornerShape(20.dp) // Boxy shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp), // Add padding to avoid clipping
            shape = RoundedCornerShape(20.dp), // Boxy shape
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, // Transparent background
                contentColor = Color.White // White text
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp) // Remove default elevation
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = text)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Arrow",
                    tint = Color.White
                )
            }
        }
    }
}

// Outlined button style (white background, light blue outline, light blue text)
@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(60.dp) // Fixed height
            .widthIn(min = 250.dp), // Minimum width
        shape = RoundedCornerShape(20.dp), // Boxy shape
        border = BorderStroke(2.dp, LightBlue), // Light blue outline
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, // White background
            contentColor = LightBlue // Light blue text
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Arrow",
                tint = LightBlue
            )
        }
    }
}