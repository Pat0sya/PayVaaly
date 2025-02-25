package com.example.payvaaly.Tools


import SignUpScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.payvaaly.FirstLayer.ProfileCompletionScreen
import com.example.payvaaly.FirstLayer.SignInScreen
import com.example.payvaaly.FirstLayer.WelcomeScreen
import com.example.payvaaly.SecondLayer.Payment
import com.example.payvaaly.SecondLayer.TitleScreen

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
                onSignInSuccess = { navController.navigate("Title") }
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
                onComplete = { navController.navigate("Title") }
            )
        }
        composable("Title") {
            TitleScreen(
                navController = navController,
                onSignOut = {
                    navController.popBackStack("Welcome", inclusive = false)
                }
            )
        }
        composable("Payment") {
            Payment(
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNavigation() {
    Navigation()
}