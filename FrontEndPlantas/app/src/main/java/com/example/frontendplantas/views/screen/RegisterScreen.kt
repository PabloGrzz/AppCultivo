package com.example.frontendplantas.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.PatternsCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.frontendplantas.R
import com.example.frontendplantas.core.RetrofitClient
import com.example.frontendplantas.models.LoginRequest
import com.example.frontendplantas.models.RegisterRequest
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.ColorGradient2
import com.example.frontendplantas.ui.theme.ColorGradient3
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.utils.AuthTokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//NECESITA MAS ESPACIO ARRIBA PARA RESPIRAR Y SOBRA MUCHO ABAJO

@Composable
fun RegisterScreen(navController: NavController,paddingValues: PaddingValues){

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    // Instancia de AuthTokenManager
    val authTokenManager = AuthTokenManager(LocalContext.current)

    // Estado para manejar errores de la API
    var apiError by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.planta_animacion))

    val progress by animateLottieCompositionAsState(
        isPlaying = true,
        composition = composition,
        iterations = 1,
        speed = 0.7f
    )
    Box(modifier = Modifier.fillMaxSize()){

        Image(
            painter = painterResource(id = R.drawable.texturahoja),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
            alpha = 0.8f
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            //La animacion debe quedarse "congelada" al acabar, no un loop
            LottieAnimation(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally),
                composition = composition,
                progress = {progress}
            )

            Text(
                text = "Crear cuenta",
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Porfavor introduzca sus datos",
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(10.dp))


            TextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        text = emailError.ifEmpty { "Email" },
                        color = if (emailError.isNotEmpty()) Red else Color.Unspecified
                    )
                },
                leadingIcon = {
                    Icon(Icons.Rounded.AccountCircle, contentDescription = "")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent
                )
            )

            TextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(
                        text = nameError.ifEmpty { "Nombre" },
                        color = if (nameError.isNotEmpty()) Red else Color.Unspecified
                    )
                },
                leadingIcon = {
                    Icon(Icons.Rounded.AccountCircle, contentDescription = "")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(passwordError.ifEmpty { "Password" },
                        color = if (passwordError.isNotEmpty()) Red else Color.Unspecified)
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Lock, contentDescription = "")
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        painterResource(id = R.drawable.visibility_24px)
                    else painterResource(id = R.drawable.visibility_off_24px)

                    Icon(
                        painter = image,
                        contentDescription = "",
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent
                )
            )

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = {
                    Text(confirmPasswordError.ifEmpty { "Confirma la Password" }, color = if (confirmPasswordError.isNotEmpty()) Red else Color.Unspecified)
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Lock, contentDescription = "")
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible)
                        painterResource(id = R.drawable.visibility_24px)
                    else painterResource(id = R.drawable.visibility_off_24px)

                    Icon(
                        painter = image,
                        contentDescription = "",
                        modifier = Modifier.clickable { confirmPasswordVisible = !confirmPasswordVisible }
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Resetear errores
                    nameError = ""
                    emailError = ""
                    passwordError = ""
                    confirmPasswordError = ""
                    apiError = ""

                    // Validaciones
                    nameError = if (name.isBlank()) "Nombre obligatorio" else ""
                    emailError = when {
                        email.isBlank() -> "Email obligatorio"
                        !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() -> "Email no válido"
                        else -> ""
                    }
                    passwordError = if (password.isBlank()) "Contraseña obligatoria" else ""
                    confirmPasswordError = when {
                        confirmPassword.isBlank() -> "Confirmar contraseña obligatoria"
                        password != confirmPassword -> "Las contraseñas no coinciden"
                        else -> ""
                    }

                    // Si todas las validaciones pasan
                    if (nameError.isEmpty() && emailError.isEmpty() &&
                        passwordError.isEmpty() && confirmPasswordError.isEmpty()) {

                        isLoading = true

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                // 1. Registrar usuario
                                val registerResponse = RetrofitClient.webService.registrarse(
                                    RegisterRequest(email, password, name)
                                )

                                if (registerResponse.isSuccessful) {
                                    // 2. Login automático para obtener token
                                    val loginResponse = RetrofitClient.webService.hacerLogin(
                                        LoginRequest(email, password)
                                    )

                                    withContext(Dispatchers.Main) {
                                        if (loginResponse.isSuccessful) {
                                            loginResponse.body()?.token?.let { token ->
                                                authTokenManager.saveToken(token)
                                                navController.navigate("plantasLista") {
                                                    popUpTo("register") { inclusive = true }
                                                }
                                            } ?: run {
                                                apiError = "Error: No se recibió token"
                                            }
                                        } else {
                                            apiError = "Error en login automático"
                                        }
                                        isLoading = false
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        apiError = "Error en registro: ${registerResponse.errorBody()?.string() ?: "Error desconocido"}"
                                        isLoading = false
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    apiError = "Error de conexión: ${e.message}"
                                    isLoading = false
                                }
                            }
                        }
                    }
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Registrarse")
                }
            }

            // Mostrar errores de API
            if (apiError.isNotEmpty()) {
                Text(
                    text = apiError,
                    color = Red,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = {navController.navigate("Login")}) {
                Text("Ya estas registrado? Accede")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    val navController = rememberNavController()  // Crear un controlador de navegación ficticio
    FrontEndPlantasTheme {
        RegisterScreen(navController = navController, paddingValues = PaddingValues(16.dp))
    }
}
