package com.example.payvaaly.Tools


import SignUpScreen
import TransactionsScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.payvaaly.FirstLayer.SignInScreen
import com.example.payvaaly.FirstLayer.WelcomeScreen
import com.example.payvaaly.SecondLayer.MyCards
import com.example.payvaaly.SecondLayer.Payment
import com.example.payvaaly.SecondLayer.TitleScreen
import com.example.payvaaly.tools.fetchUsersFromApi

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val isDarkTheme = remember { mutableStateOf(false) }

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
                onNavigateToProfile = { navController.navigate("Title") }
            )
        }

        composable("Title") {
            TitleScreen(
                navController = navController,
                onSignOut = { navController.popBackStack("Welcome", inclusive = false) },
                isDarkTheme = isDarkTheme.value,
                onToggleTheme = { isDarkTheme.value = !isDarkTheme.value
                }
            )
        }
        composable("Payment") {
            Payment(
                onBackClicked = { navController.popBackStack() },
                fetchUsers = { fetchUsersFromApi() }
            )
        }
        composable("MyCards") {
            MyCards(
                onBackClicked = { navController.popBackStack() }
            )
        }
        composable("Transactions") {  // Добавленный экран
            TransactionsScreen(onBackClicked = { navController.popBackStack() })
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewNavigation() {
    Navigation()
}