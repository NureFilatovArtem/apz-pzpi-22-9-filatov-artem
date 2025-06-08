package com.artemfilatov.environmentmonitor.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var languageExpanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("🇺🇦 Українська") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "🌿 Система моніторингу",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка Увійти
            Button(
                onClick = { onLoginSuccess() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Увійти")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Кнопка Увійти як гість
            TextButton(onClick = { onLoginSuccess() }) {
                Text("Увійти як гість")
            }
        }
        // Кнопка Зареєструватися
            TextButton(onClick = { /* TODO: register logic */ }) {
                Text("Зареєструватися")
            }

        // 🔽 Admin Login
        TextButton(
            onClick = { /* TODO: admin login */ },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        ) {
            Text("👮‍♂️ Увійти як адміністратор", fontSize = 13.sp)
        }

        // 🌐 Language Dropdown
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            TextButton(onClick = { languageExpanded = true }) {
                Text(selectedLanguage, fontSize = 13.sp)
            }

            DropdownMenu(
                expanded = languageExpanded,
                onDismissRequest = { languageExpanded = false },
                properties = PopupProperties(focusable = true)
            ) {
                DropdownMenuItem(
                    text = { Text("🇺🇦 Українська") },
                    onClick = {
                        selectedLanguage = "🇺🇦 Українська"
                        languageExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("🇬🇧 English") },
                    onClick = {
                        selectedLanguage = "🇬🇧 English"
                        languageExpanded = false
                    }
                )
            }
        }
    }
}