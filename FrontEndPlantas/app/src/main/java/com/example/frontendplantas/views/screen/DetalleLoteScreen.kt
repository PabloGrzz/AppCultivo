package com.example.frontendplantas.views.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.frontendplantas.models.Lote
import com.example.frontendplantas.models.Planta
import com.example.frontendplantas.models.ProcesoConFecha
import com.example.frontendplantas.ui.theme.Dark
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.utils.AuthTokenManager
import com.example.frontendplantas.utils.FileManager
import com.example.frontendplantas.utils.ImageCompressor
import com.example.frontendplantas.viewmodels.LoteViewModel
import com.example.frontendplantas.views.screen.components.DescripcionLote
import com.example.frontendplantas.views.screen.components.FormularioAñadirProceso
import com.example.frontendplantas.views.screen.components.HeaderPrincipal
import com.example.frontendplantas.views.screen.components.ImageSelector
import com.example.frontendplantas.views.screen.components.PrincipalLote
import android.util.Log

private const val TAG = "DetalleLoteScreen"

@Composable
fun DetalleLoteScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    loteId: Long,
    authTokenManager: AuthTokenManager = AuthTokenManager(LocalContext.current)
) {
    val elevation = remember { mutableStateOf(5.dp) }
    var popupManager by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewModel: LoteViewModel = viewModel()
    val scrollState = rememberScrollState()

    val userRole = authTokenManager.getUserRole()
    val isAdmin = userRole == "ROLE_ADMIN"

    val fileManager = remember { FileManager(context) }

    // Cargar datos cuando se inicia la pantalla
    LaunchedEffect(loteId) {
        Log.d(TAG, "Loading lote with ID: $loteId")
        viewModel.getLoteById(loteId)
    }

    val loteState by viewModel.loteState.observeAsState(LoteViewModel.LoteState.Loading)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = elevation.value, spotColor = Dark)
            ) {
                HeaderPrincipal()
            }
        },
        content = { paddings ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
            ) {
                when (loteState) {
                    is LoteViewModel.LoteState.Loading -> {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                    is LoteViewModel.LoteState.Error -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Error al cargar el lote")
                            Button(onClick = { navController.popBackStack() }) {
                                Text("Volver")
                            }
                        }
                    }
                    is LoteViewModel.LoteState.Success -> {
                        val lote = (loteState as LoteViewModel.LoteState.Success).lote

                        if (lote == null) {
                            Text("No se encontró el lote", Modifier.align(Alignment.Center))
                        } else {
                            Column(
                                modifier = Modifier.verticalScroll(scrollState)
                            ) {
                                PrincipalLote(lote = lote)
                                Spacer(Modifier.height(18.dp))
                                Column(Modifier.padding(12.dp)) {
                                    DescripcionLote(lote = lote)
                                }
                                Box(Modifier.align(Alignment.CenterHorizontally)) {
                                    // Solo mostramos este botón si tenemos una planta válida
                                    lote.planta?.id?.let { plantaId ->
                                        Button(onClick = {
                                            navController.navigate("plantaDetallesScreen/$plantaId")
                                        }) {
                                            Text("Información planta")
                                        }
                                        Spacer(Modifier.width(14.dp))
                                    }
                                }
                                // Botón "Añadir proceso" - solo para admin
                                if (isAdmin) {
                                    Box(Modifier.align(Alignment.CenterHorizontally)) {
                                        Button(onClick = { popupManager = true }) {
                                            Text("Añadir proceso")
                                        }
                                    }
                                }

                                // Botón "Borrar lote" - solo para admin
                                if (isAdmin) {
                                    Box(Modifier.align(Alignment.CenterHorizontally)) {
                                        Button(onClick = {
                                            viewModel.eliminarLote(loteId)
                                            navController.popBackStack()
                                        }) {
                                            Text("Borrar lote")
                                        }
                                    }
                                }

                                ImageSelector(
                                    imageCompressor = remember { ImageCompressor(context) },
                                    fileManager = remember { FileManager(context) },
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                lote.qrCode?.let { qrBase64 ->
                                    val qrBitmap = fileManager.base64ToImageBitmap(qrBase64)
                                    qrBitmap?.let {
                                        androidx.compose.foundation.Image(
                                            bitmap = it,
                                            contentDescription = "QR del lote",
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .padding(16.dp)
                                                .width(200.dp)
                                                .height(200.dp)
                                        )
                                    } ?: Text(
                                        "No se pudo cargar el código QR",
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        color = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }

                // Overlay difuminado para el formulario
                AnimatedVisibility(
                    visible = popupManager,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .blur(radius = 8.dp)
                    )
                }

                // Formulario para añadir proceso
                AnimatedVisibility(
                    visible = popupManager,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            shape = RoundedCornerShape(16.dp),
                            shadowElevation = 8.dp
                        ) {
                            FormularioAñadirProceso(
                                paddingValues = PaddingValues(0.dp),
                                onDismiss = { popupManager = false },
                                onAddProceso = { proceso ->
                                    viewModel.agregarProcesoALote(loteId, proceso)
                                    popupManager = false
                                },
                                loteId = loteId
                            )
                        }
                    }
                }
            }
        }
    )
}