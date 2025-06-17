package com.example.frontendplantas.views

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid // Este es el q se usa ahora para lazyVerticalGrid!!
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.frontendplantas.ui.theme.VerdeClaro
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendplantas.viewmodels.MainViewModel
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy


val numeroColumnas = GridCells.Fixed (3)
val randomSizedPhotos = listOf(
    "https://example.com/photo1.jpg",
    "https://example.com/photo2.jpg",
    "https://example.com/photo3.jpg"
)


@Composable
fun PantallaListarPlantas(navController: NavController,paddingValues: PaddingValues) {

    val context = LocalContext.current

    val viewModel: MainViewModel = viewModel()

    var qrResult by remember { mutableStateOf("") }

    // Llamar a un ViewModel o función que utilice el contexto
    //val viewModel = viewModel<PlantasViewModel>()
    //viewModel.instalarEscaner(context)

    // Estado para manejar el menú desplegable
    var expanded by remember { mutableStateOf(false) }

    // Estado para manejar las opciones seleccionadas
    var selectedOption by remember { mutableStateOf("") }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                // Aquí puedes manejar la imagen obtenida (por ejemplo, pasarla al ViewModel)
                // viewModel.processImageBitmap(imageBitmap)
                Toast.makeText(context, "Imagen capturada", Toast.LENGTH_SHORT).show()
            }
        }
    }



    // Llamada al méodo para comenzar a escanear QR cuando qrResult cambie
    /*LaunchedEffect(qrResult) {

        viewModel.checkAndInitScanner(context)
    }*/


    /*LaunchedEffect(Unit) {
        viewModel.startScanRx()  // Llamamos al méodo para que comience el escaneo
            .subscribeBy(
                onNext = { result ->
                    // Aquí manejamos el resultado del escaneo
                    qrResult = result
                    println("Resultado del escaneo: $qrResult")
                },
                onError = { error ->
                    // Aquí manejamos cualquier error durante el escaneo
                    println("Error al escanear: ${error.message}")
                },
                onComplete = {
                    // Opcionalmente manejamos la finalización del escaneo
                    println("Escaneo completado")
                }
            )
    }*/

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            // Espacio
            Spacer(modifier = Modifier.height(50.dp))

            // Campo de búsqueda
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Planta") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                trailingIcon = {
                    // Aquí iría el icono de búsqueda (puedes usar un Image o Icon)
                    Text("🔍")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onDone = { /* Acción al presionar Done */ })
            )

            Spacer(modifier = Modifier.height(15.dp))

            LazyVerticalGrid(
                columns = numeroColumnas,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(0.dp)
            ) {

                items(List(20) { "Planta $it" }) { item ->
                    PlantaCard(name = item)
                }
            }

            //este layzyVertical esta configurado para adaptarse al tamaño de las imagenes, es responsive al tamaño
            /*LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                        //aqui iria la lista de imagenes de las plantas
                    items(randomSizedPhotos.size) { photo ->
                        AsyncImage(
                            model = photo, // si es en local usaremos R.drawable.tu_imagen... tu sabeh
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )*/


        }

        // Botón flotante, siempre en la parte superior derecha
        FloatingActionButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 24.dp, start = 16.dp)
        ) {
            Text("☰")
        }


        // Opciones adicionales cuando se expande el menú
        AnimatedVisibility(
            visible = expanded,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 100.dp, start = 16.dp)
        ) {
            Column(

            ) {
                FloatingActionButton(onClick = {
                    selectedOption = "Opción 1" // Este es un cambio clave, ahora actualizamos la opción seleccionada
                    expanded = false // Al seleccionar la opción, cerramos el menú, veremos xq quiero hacer una animacion de las que se expande a toda la pantalla y te lleva a la camara or whatever

                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(cameraIntent)

                }) {
                    Text("Camara")
                }
                Spacer(modifier = Modifier.height(8.dp))
                /*FloatingActionButton(onClick = {
                    //Esto deberia ir en el viewmodel
                    expanded = false // Cierra el menú
                    //viewModel.installGoogleScannerRx(context) // Instala el módulo si es necesario
                    viewModel.startScanRx()
                        .subscribeBy(
                            onNext = { result ->
                                qrResult = result
                                println("Resultado del escaneo QR: $qrResult")
                                Toast.makeText(context, "Escaneo exitoso: $qrResult", Toast.LENGTH_SHORT).show()
                            },
                            onError = { error ->
                                println("Fallo al escanear: ${error.message}")
                                Toast.makeText(context, "Error al escanear: ${error.message}", Toast.LENGTH_SHORT).show()
                            },
                            onComplete = {
                                println("Escaneo exitoso")
                            }
                        )
                }) {
                    Text("QR´s") // el QR tarda un monton en desplegarse en la pantalla, igual no es la mejor idea hacer tdo en un hilo secundario??
                }*/
            }

        }
    }
}

@Composable
fun PlantaCard(name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .background(VerdeClaro)

        ) {
            // Imagen placeholder (reemplaza con tu imagen real)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp) // esto si quieres q sea responsive igual esta mal o QQQQQQQQ!!!!!
                    .wrapContentSize()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ) // Esta es la imagen representada por un color whaita
            ) {
                Text(
                    "Imagen de ${name}",
                    modifier = Modifier.align(Alignment.Center).fillMaxHeight()
                )


                //val painter = rememberImagePainter(imageUrl)

                /* Image(
                    painter = painter,
                    contentDescription = "Imagen de ${name}",
                    modifier = Modifier.fillMaxSize().wrapContentSize(),
                    alignment = Alignment.Center,
                    //contentScale = androidx.compose.ui.layout.ContentScale.Crop // Asegura que la imagen se recorte si es necesario
                    contentScale = ContentScale.Fit // Ajusta la imagen para que no se distorsione

                )*/
            }

            // Texto debajo de la imagen
            Text(
                text = name,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
                    .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp)),

                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}


//Los preview puedes ejecutarlos en el emulador

@Preview(showBackground = true)
@Composable
fun PreviewPantallaListarPlantas() {

    val navController = rememberNavController()
    PantallaListarPlantas(navController = navController, paddingValues = PaddingValues(16.dp))
}
