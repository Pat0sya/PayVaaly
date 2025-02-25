package com.example.payvaaly.FirstLayer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.payvaaly.R
import com.example.payvaaly.ui.theme.UnderlineTextField

@Composable
fun ProfileCompletionScreen(onComplete: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePictureUri = uri
    }

    // Define the gradient colors
    val gradientColors = listOf(
        Color(0xFF2D64FF), // Dark blue
        Color(0xFF2D4BFF), // Light blue
        Color(0xFF2D32FF)  // Lighter blue
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = gradientColors) // Vertical gradient
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Black "Complete Your Profile" text
            Text(
                text = "Завершите создание!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start) // Align to the start (left)
                    .padding(bottom = 16.dp) // Add spacing below
            )

            // Profile Picture Section
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
            ) {
                if (profilePictureUri != null) {
                    AsyncImage(
                        model = profilePictureUri, // Use `model` instead of `data`
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_assignment_ind_24), // Placeholder image
                        contentDescription = "Profile Picture Placeholder",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Add Profile Picture Button (OutlinedButton style)
            OutlinedButton(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Добавить аватар",color = Color.White,)
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Username
            UnderlineTextField(
                value = username,
                onValueChange = { username = it },
                label = "Имя пользователя",
                modifier = Modifier.fillMaxWidth(),


            )
            Spacer(modifier = Modifier.height(16.dp))

            // First Name
            UnderlineTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "Имя",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Last Name
            UnderlineTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Фамилия",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Date of Birth
            UnderlineTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = "Дата рождения (дд-мм-гггг)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Complete Button (Custom style: white text, light blue background, aligned to the left)
            Button(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start), // Align to the left
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // White background
                    contentColor = Color(0xFF455EE5) // Dark blue text
                )
            ) {
                Text(text = "Завершить!")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileCompletionScreen() {
    ProfileCompletionScreen(onComplete = {})
}