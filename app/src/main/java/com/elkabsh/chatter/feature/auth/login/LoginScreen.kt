package com.elkabsh.chatter.feature.auth.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elkabsh.chatter.R

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {

        when (uiState.value) {
            is LoginUiState.Success -> {
                navController.navigate("home")
            }

            is LoginUiState.Error -> {
                Toast.makeText(context, "Sign In failed", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .imePadding()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            if (uiState.value == LoginUiState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotEmpty() && password.isNotEmpty() && (uiState.value == LoginUiState.Nothing || uiState.value == LoginUiState.Error)
                ) {
                    Text(text = "Sign In")
                }

                TextButton(onClick = { navController.navigate("signup") }) {
                    Text(text = "Don't have an account? Sign Up")
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    LoginScreen(navController = rememberNavController())
}
