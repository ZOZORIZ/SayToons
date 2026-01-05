package com.saytoons.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saytoons.app.LoginViewModel
import com.saytoons.app.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val loginState by viewModel.loginState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showError = loginState is LoginViewModel.LoginState.Error

    LaunchedEffect(loginState) {
        if (loginState is LoginViewModel.LoginState.Success) {
            onLoginSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(280.dp)
                .offset(y = 170.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "parent@gmail.com",
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email Icon", tint = Color(0xFF64B5F6))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Password Icon", tint = Color(0xFF90A4AE))
                    },
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = "Toggle password visibility", tint = Color(0xFF90A4AE))
                        }
                    },
                    visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.login_button),
                    contentDescription = "Login",
                    modifier = Modifier
                        .width(250.dp)
                        .alpha(if (isLoading) 0.5f else 1f)
                        .clickable(
                            enabled = !isLoading,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            viewModel.login(email, password)
                        },
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Forgot password?",
                    color = Color(0xFF1976D2),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable(
                        enabled = !isLoading,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* TODO: Handle forgot password */ }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text("New here?", color = Color.Gray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.parent_acc),
                    contentDescription = "Create Parent Account",
                    modifier = Modifier
                        .alpha(if (isLoading) 0.5f else 1f)
                        .clickable(
                            enabled = !isLoading,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onCreateAccountClick
                        ),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (showError) (loginState as LoginViewModel.LoginState.Error).message else "",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.alpha(if (showError) 1f else 0f)
                )
            }
        }

        SayToonsBackButton(onClick = onBackClick)
    }
}
