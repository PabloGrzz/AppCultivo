package com.example.frontendplantas.views.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.frontendplantas.models.Planta
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.ColorGradient2
import com.example.frontendplantas.ui.theme.ColorGradient3
import com.example.frontendplantas.ui.theme.Dark
import com.example.frontendplantas.ui.theme.Dark1
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.ui.theme.Pink
import com.example.frontendplantas.ui.theme.Pink1
import com.example.frontendplantas.viewmodels.PlantasViewModel
import androidx.compose.runtime.livedata.observeAsState
import coil.compose.AsyncImage

@Composable
fun PlantListComponent(
    viewModel: PlantasViewModel = viewModel(),
    onPlantClick: (Planta) -> Unit = {}
) {
    val plantas by viewModel.plantasFiltradas.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    // Efecto para cargar datos
    LaunchedEffect(Unit) {
        println("🏁 Iniciando carga de plantas")
        if (plantas.isEmpty()) {
            viewModel.obtenerPlantas()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            error != null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: $error")
                    Button(onClick = { viewModel.obtenerPlantas() }) {
                        Text("Reintentar")
                    }
                }
            }
            plantas.isEmpty() -> {
                Text(
                    "No hay plantas disponibles",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(plantas) { planta ->
                        PlantItem(
                            planta = planta,
                            invertLayout = plantas.indexOf(planta) % 2 == 0,
                            onClick = { onPlantClick(planta) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlantItem(
    planta: Planta,
    invertLayout: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        ProductBackground(
            Modifier.padding(bottom = 24.dp),
            invertLayout = invertLayout
        )
        PlantContent(
            planta = planta,
            invertLayout = invertLayout,
            modifier = Modifier,
            onClick = onClick
        )
    }
}

@Composable
private fun PlantContent(
    planta: Planta,
    invertLayout: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        val imageAlignment = if (invertLayout) Alignment.TopStart else Alignment.TopEnd
        val textAlignment = if (invertLayout) Alignment.BottomEnd else Alignment.BottomStart

        if (!planta.imagenes.isNullOrEmpty() && planta.imagenes[0].datos != null) {
            val primeraImagen = planta.imagenes[0]
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("data:${primeraImagen.tipoMime ?: "image/jpeg"};base64,${primeraImagen.datos}")
                    .build(),
                contentDescription = "Imagen de ${planta.nombre}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .align(imageAlignment),
                error = painterResource(android.R.drawable.ic_menu_gallery),
                placeholder = painterResource(android.R.drawable.ic_menu_gallery)
            )
        } else {
            Image(
                painter = painterResource(android.R.drawable.ic_menu_gallery),
                contentDescription = "Imagen no disponible",
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .align(imageAlignment)
            )
        }

        Column(
            modifier = Modifier
                .align(textAlignment)
                .padding(
                    bottom = 16.dp,
                    start = if(invertLayout) 16.dp else 32.dp,
                    end = if(invertLayout) 32.dp else 16.dp
                )
                .fillMaxWidth(0.75f)
        ) {
            Surface(
                modifier = Modifier.padding(
                    start = if (invertLayout) 50.dp else 16.dp
                ).shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Pink
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = planta.nombre ?: "Nombre no disponible",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
                    color = Dark,
                )
            }

            Surface(
                modifier = Modifier.padding(
                    top = 8.dp,
                    start = if(invertLayout) 50.dp else 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Pink1
            ) {
                Text(
                    text = planta.nombreCientifico ?: "",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Dark
                )
            }

            Surface(
                modifier = Modifier.padding(
                    top = 8.dp,
                    start = if(invertLayout) 50.dp else 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Pink1
            ) {
                Text(
                    text = "Precio: ${planta.precio ?: 0.0}€",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Dark
                )
            }
        }
    }
}

@Composable
private fun ProductBackground(
    modifier: Modifier = Modifier,
    invertLayout: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .border(width = 1.dp, shape = RoundedCornerShape(120.dp), color = Dark1)
            .background(
                brush = Brush.linearGradient(
                    colors = if (invertLayout) {
                        listOf(
                            ColorGradient3,
                            ColorGradient2,
                            ColorGradient1
                        )
                    } else {
                        listOf(
                            ColorGradient1,
                            ColorGradient2,
                            ColorGradient3
                        )
                    },
                ),
                shape = RoundedCornerShape(120.dp)
            )
    )
}

@Preview(showBackground = true)
@Composable
fun PlantListComponentPreview() {
    FrontEndPlantasTheme {
        val mockViewModel = PlantasViewModel().apply {
            _listaPlantas.value = listOf(
                Planta(
                    id = 1,
                    nombre = "Rosa",
                    nombreCientifico = "Rosa spp.",
                    precio = 15.99,
                    imagenes = emptyList()
                ),
                Planta(
                    id = 2,
                    nombre = "Lavanda",
                    nombreCientifico = "Lavandula angustifolia",
                    precio = 12.50,
                    imagenes = emptyList()
                )
            )
        }

        PlantListComponent(viewModel = mockViewModel)
    }
}