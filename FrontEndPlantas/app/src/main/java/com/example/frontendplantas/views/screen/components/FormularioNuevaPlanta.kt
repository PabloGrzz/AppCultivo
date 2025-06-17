package com.example.frontendplantas.views.screen.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontendplantas.models.Planta
import com.example.frontendplantas.ui.theme.Dark
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.utils.FileManager
import com.example.frontendplantas.utils.ImageCompressor
import com.example.frontendplantas.viewmodels.ImagenesViewModel
import com.example.frontendplantas.viewmodels.PlantasViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@Preview(showBackground = true)
@Composable
fun FormularioNuevaPlantaPreview() {
    val navController = rememberNavController()
    FrontEndPlantasTheme {
        FormularioNuevaPlanta(
            modifier = Modifier,
            navController = navController,
            paddingValues = PaddingValues(10.dp)
        )
    }
}

@Composable
fun FormularioNuevaPlanta(
    modifier: Modifier,
    navController: NavController,
    paddingValues: PaddingValues,
    plantasViewModel: PlantasViewModel = viewModel(),
    imagenesViewModel: ImagenesViewModel = viewModel()
) {
    // Estados para los campos de texto
    var nombre by remember { mutableStateOf("") }
    var nombreError by remember { mutableStateOf("") }

    var nombreCientifico by remember { mutableStateOf("") }
    var nombreCientificoError by remember { mutableStateOf("") }

    var familia by remember { mutableStateOf("") }
    var familiaError by remember { mutableStateOf("") }

    var frecuenciaRiego by remember { mutableStateOf("") }

    var temperatura by remember { mutableStateOf("") }
    var temperaturaError by remember { mutableStateOf("") }

    var humedad by remember { mutableStateOf("") }

    var suelo by remember { mutableStateOf("") }

    var fertilizacion by remember { mutableStateOf("") }

    var beneficios by remember { mutableStateOf("") }

    var toxicidad by remember { mutableStateOf("") }

    var precio by remember { mutableStateOf("0.0") }

    // Estados para la imagen
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Utilidades para manejo de archivos
    val context = LocalContext.current
    val fileManager = remember { FileManager(context) }
    val imageCompressor = remember { ImageCompressor(context) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Observadores del estado
    val plantaCreada by plantasViewModel.plantaCreada.observeAsState()
    val errorCreacion by plantasViewModel.errorCreacion.observeAsState()
    val imagenSubida by imagenesViewModel.imagenSubida.observeAsState()
    val errorSubida by imagenesViewModel.errorSubida.observeAsState()

    // Lanzador para seleccionar imagen
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val compressedBytes = imageCompressor.compressImage(it, 1024 * 1024) // 1MB máximo
                    withContext(Dispatchers.Main) {
                        imageBytes = compressedBytes
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        snackbarHostState.showSnackbar("Error al procesar la imagen: ${e.message}")
                    }
                }
            }
        }
    }

    // Manejar creación exitosa de planta
    LaunchedEffect(plantaCreada) {
        plantaCreada?.let { planta ->
            imageBytes?.let { bytes ->
                try {
                    isLoading = true
                    val file = fileManager.createTempFile(bytes)
                    val requestFile = file.asRequestBody("image/jpeg".toMediaType())
                    val body = MultipartBody.Part.createFormData(
                        "archivo",
                        file.name,
                        requestFile
                    )

                    imagenesViewModel.subirImagenGeneral(
                        plantaId = planta.id!!,
                        imageBytes = bytes
                    )

                } catch (e: Exception) {
                    snackbarHostState.showSnackbar("Error al preparar la imagen: ${e.message}")
                    isLoading = false
                    navController.navigate("plantasLista")
                }
            } ?: run {
                // No hay imagen, navegar directamente
                snackbarHostState.showSnackbar("Planta creada con éxito")
                navController.navigate("plantasLista")
            }
        }
    }

    // Manejar subida exitosa de imagen
    LaunchedEffect(imagenSubida) {
        if (imagenSubida == true) {
            snackbarHostState.showSnackbar("Planta e imagen guardadas con éxito")
            navController.navigate("plantasLista")
            isLoading = false
        }
    }

    // Manejar errores
    LaunchedEffect(errorCreacion, errorSubida) {
        errorCreacion?.let {
            snackbarHostState.showSnackbar(it)
            isLoading = false
        }
        errorSubida?.let {
            snackbarHostState.showSnackbar(it)
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Crear planta",
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Por favor introduzca los datos de la planta",
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Selector de imagen
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(LightGray)
                    .border(2.dp, Gray, CircleShape)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Añadir foto",
                        tint = Dark,
                        modifier = Modifier.size(40.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (imageUri != null) "Imagen seleccionada" else "Toca para seleccionar una imagen",
                fontSize = 14.sp,
                color = Dark
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campos del formulario
            CamposFormulario(
                nombre = nombre,
                onNombreChange = { nombre = it },
                nombreError = nombreError,
                nombreCientifico = nombreCientifico,
                onNombreCientificoChange = { nombreCientifico = it },
                nombreCientificoError = nombreCientificoError,
                familia = familia,
                onFamiliaChange = { familia = it },
                familiaError = familiaError,
                frecuenciaRiego = frecuenciaRiego,
                onFrecuenciaRiegoChange = { frecuenciaRiego = it },
                temperatura = temperatura,
                onTemperaturaChange = { temperatura = it },
                temperaturaError = temperaturaError,
                humedad = humedad,
                onHumedadChange = { humedad = it },
                suelo = suelo,
                onSueloChange = { suelo = it },
                fertilizacion = fertilizacion,
                onFertilizacionChange = { fertilizacion = it },
                beneficios = beneficios,
                onBeneficiosChange = { beneficios = it },
                toxicidad = toxicidad,
                onToxicidadChange = { toxicidad = it },
                precio = precio,
                onPrecioChange = { precio = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController.navigate("plantasLista") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        // Validación
                        var valid = true

                        if (nombre.isBlank()) {
                            nombreError = "Nombre obligatorio"
                            valid = false
                        } else nombreError = ""

                        if (nombreCientifico.isBlank()) {
                            nombreCientificoError = "Nombre científico obligatorio"
                            valid = false
                        } else nombreCientificoError = ""

                        if (familia.isBlank()) {
                            familiaError = "Familia obligatoria"
                            valid = false
                        } else familiaError = ""

                        if (temperatura.isBlank()) {
                            temperaturaError = "Temperatura obligatoria"
                            valid = false
                        } else temperaturaError = ""

                        if (valid) {
                            isLoading = true
                            val planta = Planta(
                                nombre = nombre,
                                nombreCientifico = nombreCientifico,
                                familia = familia,
                                frecuenciaRiego = frecuenciaRiego,
                                temperatura = temperatura,
                                humedad = humedad,
                                suelo = suelo,
                                fertilizacion = fertilizacion,
                                beneficios = beneficios,
                                toxicidad = toxicidad,
                                precio = precio.toDoubleOrNull() ?: 0.0
                            )
                            plantasViewModel.createPlanta(planta)
                            navController.navigate("plantasLista")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Crear")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Snackbar para mensajes
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = White
            ) {
                Text(data.visuals.message)
            }
        }
    }
}

@Composable
private fun CamposFormulario(
    nombre: String,
    onNombreChange: (String) -> Unit,
    nombreError: String,
    nombreCientifico: String,
    onNombreCientificoChange: (String) -> Unit,
    nombreCientificoError: String,
    familia: String,
    onFamiliaChange: (String) -> Unit,
    familiaError: String,
    frecuenciaRiego: String,
    onFrecuenciaRiegoChange: (String) -> Unit,
    temperatura: String,
    onTemperaturaChange: (String) -> Unit,
    temperaturaError: String,
    humedad: String,
    onHumedadChange: (String) -> Unit,
    suelo: String,
    onSueloChange: (String) -> Unit,
    fertilizacion: String,
    onFertilizacionChange: (String) -> Unit,
    beneficios: String,
    onBeneficiosChange: (String) -> Unit,
    toxicidad: String,
    onToxicidadChange: (String) -> Unit,
    precio: String,
    onPrecioChange: (String) -> Unit
) {
    // Campos de texto reutilizables
    CampoTexto(
        value = nombre,
        onValueChange = onNombreChange,
        label = nombreError.ifEmpty { "Nombre" },
        isError = nombreError.isNotEmpty()
    )

    CampoTexto(
        value = nombreCientifico,
        onValueChange = onNombreCientificoChange,
        label = nombreCientificoError.ifEmpty { "Nombre científico" },
        isError = nombreCientificoError.isNotEmpty()
    )

    CampoTexto(
        value = familia,
        onValueChange = onFamiliaChange,
        label = familiaError.ifEmpty { "Familia" },
        isError = familiaError.isNotEmpty()
    )

    CampoTexto(
        value = frecuenciaRiego,
        onValueChange = onFrecuenciaRiegoChange,
        label = "Frecuencia de riego"
    )

    CampoTexto(
        value = temperatura,
        onValueChange = onTemperaturaChange,
        label = temperaturaError.ifEmpty { "Temperatura adecuada" },
        isError = temperaturaError.isNotEmpty()
    )

    CampoTexto(
        value = humedad,
        onValueChange = onHumedadChange,
        label = "Humedad"
    )

    CampoTexto(
        value = suelo,
        onValueChange = onSueloChange,
        label = "Suelo"
    )

    CampoTexto(
        value = fertilizacion,
        onValueChange = onFertilizacionChange,
        label = "Fertilización"
    )

    CampoTexto(
        value = beneficios,
        onValueChange = onBeneficiosChange,
        label = "Descripción"
    )

    CampoTexto(
        value = toxicidad,
        onValueChange = onToxicidadChange,
        label = "Toxicidad"
    )

    CampoTexto(
        value = precio,
        onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) onPrecioChange(it) },
        label = "Precio"
    )
}

@Composable
private fun CampoTexto(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = if (isError) Red else Color.Unspecified) },
        leadingIcon = { Icon(Icons.Rounded.Create, contentDescription = "") },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 20.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            errorIndicatorColor = Transparent
        ),
        isError = isError
    )
    Spacer(modifier = Modifier.height(12.dp))
}