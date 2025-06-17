package com.example.frontendplantas.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.frontendplantas.R
import com.example.frontendplantas.core.RetrofitClient
import com.example.frontendplantas.models.LoginRequest
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.ColorGradient2
import com.example.frontendplantas.ui.theme.ColorGradient3
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.ui.theme.Pink
import com.example.frontendplantas.ui.theme.Pink1
import com.example.frontendplantas.utils.AuthTokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun LoginScreen(navController: NavController,paddingValues: PaddingValues) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    // Instancia de AuthTokenManager
    val authTokenManager = AuthTokenManager(LocalContext.current)

    // Estado para manejar errores de la API
    var apiError by remember { mutableStateOf("") }

    val emailLabelColor = if (emailError.isNotEmpty()) Red else Unspecified
    val passwordLabelColor = if (passwordError.isNotEmpty()) Red else Unspecified

    var isLoading by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.planta_animacion))

    val progress by animateLottieCompositionAsState(
        isPlaying = true,
        composition = composition,
        iterations = 1,
        speed = 0.7f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){

        Image(
            painter = painterResource(id = R.drawable.texturahoja),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
            alpha = 0.8f
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        LottieAnimation(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.CenterHorizontally),
            composition = composition,
            progress = {progress}
        )



        Text(text = "Login", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Pink)

        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    text = emailError.ifEmpty { "Nombre" },
                    color = emailLabelColor
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



        Spacer(modifier = Modifier.height(8.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(passwordError.ifEmpty { "Password" }, color = passwordLabelColor)
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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                emailError = if (email.isBlank()) "Email requerido" else ""
                passwordError = if (password.isBlank()) "Contraseña requerida" else ""

                if (emailError.isEmpty() && passwordError.isEmpty()) {
                    isLoading = true
                    apiError = ""

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = RetrofitClient.webService.hacerLogin(
                                LoginRequest(email, password)
                            )

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    response.body()?.token?.let { token ->
                                        RetrofitClient.updateToken(token)
                                        navController.navigate("plantasLista") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } ?: run {
                                        apiError = "Error en la autenticación"
                                    }
                                } else {
                                    apiError = when (response.code()) {
                                        401 -> "Credenciales incorrectas"
                                        else -> "Error: ${response.message()}"
                                    }
                                }
                                isLoading = false
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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 90.dp)
        ) {
            Text("Login")
        }

        if (apiError.isNotEmpty()) {
            Text(text = apiError, color = Color.Red, modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Contraseña olvidada?",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                // Logic for forgot password
            }
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row {
            //El negro habra q cambiarlo probablemente o ponerle un bold
            Text(text = "No estás registrado?")
            Text(
                text = "¡Regístrate ahora!",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { navController.navigate("register")
                }
            )
        }
    }
        }
}
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val navControler = rememberNavController()

    FrontEndPlantasTheme {
        LoginScreen(
            navControler,
            paddingValues = PaddingValues(16.dp)
        )
    }
}
