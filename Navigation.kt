package com.example.payvaaly


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Welcome") {
        composable("Welcome") {
            WelcomeScreen(
                onSignInClicked = { navController.navigate("SignIn") },
                onSignUpClicked = { navController.navigate("SignUp") }
            )
        }
        composable("SignIn") {
            SignInScreen(
                onBackClicked = { navController.popBackStack() },
                onSignInSuccess = { navController.navigate("Title") } // Navigate to TitleScreen after sign-in
            )
        }
        composable("SignUp") {
            SignUpScreen(
                onBackClicked = { navController.popBackStack() },
                onNavigateToProfile = { navController.navigate("ProfileCompletion") }
            )
        }
        composable("ProfileCompletion") {
            ProfileCompletionScreen(
                onComplete = { navController.navigate("Title") } // Navigate to TitleScreen after completion
            )
        }
        composable("Title") {
            TitleScreen(navController)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNavigation() {
    Navigation()
}