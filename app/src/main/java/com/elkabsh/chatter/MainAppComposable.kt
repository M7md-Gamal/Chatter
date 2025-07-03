package com.elkabsh.chatter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elkabsh.chatter.feature.auth.login.LoginScreen
import com.elkabsh.chatter.feature.auth.signup.SignUpScreen
import com.elkabsh.chatter.feature.chat.ChatScreen
import com.elkabsh.chatter.feature.home.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val startDestination = if (currentUser != null) { "home" } else { "login" }

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("signup") {
                SignUpScreen(navController = navController)
            }
            composable("home") {
                HomeScreen(navController = navController)

            }
            composable("chat/{channelId}", arguments = listOf(
                navArgument("channelId") {
                    type = NavType.StringType
                }
            )) {
                val channelId = it.arguments?.getString("channelId") ?: ""
                ChatScreen(navController, channelId)
            }
        }

    }

}