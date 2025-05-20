package com.example.payvaaly.Tools


import SignUpScreen
import TransactionsScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.payvaaly.FirstLayer.SignInScreen
import com.example.payvaaly.FirstLayer.WelcomeScreen
import com.example.payvaaly.SecondLayer.MyCards
import com.example.payvaaly.SecondLayer.Payment
import com.example.payvaaly.SecondLayer.TitleScreen

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
                onSignInSuccess = { email ->
                    navController.navigate("Title?email=$email")
                }
            )
        }

        composable("SignUp") {
            SignUpScreen(
                onBackClicked = { navController.popBackStack() },
                onNavigateToProfile = { email ->
                    navController.navigate("Title?email=$email")
                }
            )
        }

        composable(
            "Title?email={email}",
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            Log.d("BALANCE_DEBUG", "Email = $email")
            TitleScreen(
                navController = navController,
                onSignOut = { navController.popBackStack("Welcome", inclusive = false) },
                isDarkTheme = isDarkTheme.value,
                onToggleTheme = { isDarkTheme.value = !isDarkTheme.value },
                email = email
            )
        }

        // Остальные экраны
        composable("Payment") {

            Payment(
                onBackClicked = { navController.popBackStack() },
                fetchUsers = { fetchUsersFromApi() },
                performTransaction = { recipientEmail, amount ->
                    performTransaction(recipientEmail, amount)
                }
            )
        }
        composable("MyCards") {
            MyCards(
                onBackClicked = { navController.popBackStack() }
            )
        }
        composable("Transactions") {
            TransactionsScreen(onBackClicked = { navController.popBackStack() })
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewNavigation() {
    Navigation()
}