package com.example.frontendplantas.views.screen.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import com.example.frontendplantas.R
import com.example.frontendplantas.core.Constants
import com.example.frontendplantas.ui.theme.ColorGradient2
import com.example.frontendplantas.ui.theme.ColorGradient3
import com.example.frontendplantas.ui.theme.Green
import com.example.frontendplantas.ui.theme.Pink
import com.example.frontendplantas.ui.theme.Pink1
import com.example.frontendplantas.ui.theme.YellowLight
import com.example.frontendplantas.viewmodels.MainViewModel
import com.example.frontendplantas.views.screen.DetalleLoteScreen
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import io.reactivex.rxjava3.kotlin.subscribeBy

private const val TAG = "FlotanteButton"


@Composable
fun FlotanteButton(
    modifier: Modifier,
    alignment: Alignment = Alignment.BottomStart,
    isAdmin: Boolean = false,
    navController: NavController
) {

    Log.d(TAG, "FlotanteButton: isAdmin = $isAdmin")


    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    var resultScanner by remember { mutableStateOf("") }
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            result.contents?.let { qrContent ->
                resultScanner = qrContent
                Log.d(TAG, "QR content: $qrContent")

                if (qrContent.isNotBlank()) {
                    try {
                        val url = Uri.parse(qrContent)
                        Log.d(TAG, "Parsed URL: $url, path: ${url.path}")

                        when {
                            //qrContent.startsWith("https://1bd7-139-47-52-187.ngrok-free.app/api/v1/") -> { // esta funciona para el lote id 28
                            qrContent.startsWith(Constants.BASE_URL) -> {
                                try {
                                    // Extraer el ID de forma más segura
                                    val path = url.path ?: ""
                                    val segments = path.split("/")
                                    val loteId = segments.lastOrNull()

                                    if (!loteId.isNullOrEmpty()) {
                                        Log.d(TAG, "Navigating to lote with ID: $loteId")

                                        navController.navigate("lote/$loteId")
                                    } else {
                                        Toast.makeText(context, "ID de lote no encontrado en la URL", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error al procesar la URL del QR", e)
                                    Toast.makeText(context, "Error al procesar QR: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            else -> {
                                Toast.makeText(context, "URL externa detectada: $qrContent", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing QR content", e)
                        Toast.makeText(context, "Contenido QR no válido: $qrContent", Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: run {
                Toast.makeText(context, "No se detectó código QR", Toast.LENGTH_SHORT).show()
            }
        }
    )

    //el cameraLauncher para la camara
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                Toast.makeText(context, "Imagen capturada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ViewModel setup for scanning
    val viewModel: MainViewModel = viewModel()
    val scanState by viewModel.scanState.collectAsState()

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (!viewModel.isScannerReady) {
                viewModel.initializeScanner(context)
            } else {
                viewModel.startScan()
            }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = modifier) {

        LaunchedEffect(isAdmin) {
            Log.d(TAG, "FlotanteButton LaunchedEffect: isAdmin = $isAdmin")
        }

        FloatingActionButton(
            containerColor = Color.White,
            onClick = {
                Log.d(TAG, "Main button clicked, toggling menu. isAdmin = $isAdmin")
                expanded = !expanded
            },
            modifier = Modifier.align(alignment).shadow(elevation = 10.dp, shape = RoundedCornerShape(32.dp))
        ) {
            Text("☰")
        }

        AnimatedVisibility(
            visible = expanded,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(alignment).padding(bottom = 70.dp)
        ) {
            Column {

                Log.d(TAG, "Menu expanded, isAdmin = $isAdmin")


                Spacer(modifier = Modifier.height(8.dp))


                FloatingActionButton(
                    shape = RoundedCornerShape(32.dp),
                    containerColor = Color.White,
                    onClick = {
                        expanded = false
                        scanLauncher.launch(ScanOptions())
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.qrcode_scan),
                        contentDescription = "Escanear código QR",
                        modifier = Modifier.height(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                FloatingActionButton(
                    shape = RoundedCornerShape(32.dp),
                    containerColor = Color.White,
                    onClick = {
                        expanded = false
                        navController.navigate("plantasLista")
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Escanear código QR",
                        modifier = Modifier.height(44.dp)
                    )
                }


                if (isAdmin) {
                    Spacer(modifier = Modifier.height(8.dp))

                    FloatingActionButton(
                        shape = RoundedCornerShape(32.dp),
                        containerColor = Pink1,
                        onClick = {
                            expanded = false
                            navController.navigate("nuevaPlanta") // Navega a la pantalla de creación
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_uv_index), // Asegúrate de tener este icono
                            contentDescription = "Crear planta",
                            modifier = Modifier.height(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    FloatingActionButton(
                        shape = RoundedCornerShape(32.dp),
                        containerColor = YellowLight,
                        onClick = {
                            expanded = false
                            navController.navigate("nuevoLote") // Navega a crear lote
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_o3), // Usa un ícono existente o añade uno
                            contentDescription = "Crear lote",
                            modifier = Modifier.height(44.dp)
                        )
                    }
                }
            }
        }
    }
}