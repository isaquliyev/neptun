package com.isaguliyev.neptun.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.isaguliyev.neptun.R
import com.isaguliyev.neptun.ui.theme.NeptunTheme

@Composable
fun LoginScreen(
    onLoginSuccess: (username: String, password: String, pairingKey: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var pairingKey by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val fieldShape = RoundedCornerShape(28.dp)
    val buttonShape = RoundedCornerShape(24.dp)
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFFFFFFFF),
        unfocusedBorderColor = Color(0xFFFFE082),
        focusedContainerColor = Color(0xFFFDF7EA),
        unfocusedContainerColor = Color(0xFFFDF7EA),
        focusedLabelColor = Color(0xFFFFFFFF),
        unfocusedLabelColor = Color(0xFF5E4A87),
        focusedTextColor = Color(0xFF251747),
        unfocusedTextColor = Color(0xFF251747),
        focusedTrailingIconColor = Color(0xFF5E4A87),
        unfocusedTrailingIconColor = Color(0xFF5E4A87),
        cursorColor = Color(0xFF432B73)
    )

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.neptun_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x66171535))
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(vertical = 16.dp)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Username") },
                singleLine = true,
                shape = fieldShape,
                colors = fieldColors,
                textStyle = TextStyle(fontSize = 17.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                shape = fieldShape,
                colors = fieldColors,
                textStyle = TextStyle(fontSize = 17.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            OutlinedTextField(
                value = pairingKey,
                onValueChange = { pairingKey = it },
                placeholder = { Text("Pairing key") },
                maxLines = 3,
                shape = fieldShape,
                colors = fieldColors,
                textStyle = TextStyle(fontSize = 17.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onLoginSuccess(username, password, pairingKey)
                },
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF66BB6A),
                    contentColor = Color(0xFF10260D)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "OK",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NeptunTheme {
        LoginScreen(onLoginSuccess = { _, _, _ -> })
    }
}
