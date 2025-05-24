package com.example.payvaaly.Tools


import SignUpScreen
import TransactionsScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.payvaaly.FirstLayer.SignInScreen
import com.example.payvaaly.FirstLayer.WelcomeScreen
import com.example.payvaaly.SecondLayer.MyCards
import com.example.payvaaly.SecondLayer.PaymentScreen
import com.example.payvaaly.SecondLayer.TitleScreen
import com.example.payvaaly.ThirdLayer.ContactScreen
import com.example.payvaaly.ThirdLayer.ProfileScreen

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
        composable("Payment/{ownerEmail}") { backStackEntry ->
            val ownerEmail = backStackEntry.arguments?.getString("ownerEmail") ?: ""

            PaymentScreen(
                ownerEmail = ownerEmail,
                onBackClicked = { navController.popBackStack() },
                performTransaction = { recipientEmail, amount ->
                    performTransaction(
                        senderEmail = ownerEmail,
                        recipientEmail = recipientEmail,
                        amountRubles = amount
                    )
                }
            )
        }
        composable("MyCards") {
            MyCards(
                onBackClicked = { navController.popBackStack() }
            )
        }
        composable(
            "Transactions?email={email}",
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            TransactionsScreen(
                email = email,
                onBackClicked = { navController.popBackStack() }
            )
        }
        composable(
            "Profile?email={email}",
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val emailArg = backStackEntry.arguments?.getString("email") ?: ""
            Log.d("NAV_TO_PROFILE", "ProfileScreen NavHost получил email: '$emailArg'")

            ProfileScreen(
                navController = navController,
                email = emailArg,
                onSave = { currentEmail, newFirstName, newSecondName ->
                    updateUserProfileOnServer(currentEmail, newFirstName, newSecondName)
                },

            )
        }
        composable(
            "contacts?ownerEmail={ownerEmail}",
            arguments = listOf(navArgument("ownerEmail") { defaultValue = "" })
        ) { backStackEntry ->
            val ownerEmail = backStackEntry.arguments?.getString("ownerEmail") ?: ""
            ContactScreen(
                ownerEmail = ownerEmail,
                navController = navController,
                isDarkTheme = isDarkTheme.value
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewNavigation() {
    Navigation()
}