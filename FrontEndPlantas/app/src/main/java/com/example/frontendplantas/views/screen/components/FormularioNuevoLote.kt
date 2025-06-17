package com.example.frontendplantas.views.screen.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontendplantas.models.Lote
import com.example.frontendplantas.models.Planta
import com.example.frontendplantas.models.ProcesoConFecha
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.viewmodels.LoteViewModel
import com.example.frontendplantas.viewmodels.PlantasViewModel


@Composable
fun FormularioNuevoLote(
    paddingValues: PaddingValues,
    onDismiss: () -> Unit,
    loteViewModel: LoteViewModel,
    plantasViewModel: PlantasViewModel = viewModel(),
    navController: NavController
){
    // Estado para el diálogo de selección de plantas
    var showPlantSelectorDialog by remember { mutableStateOf(false) }

    // Estado para almacenar la planta seleccionada
    var selectedPlanta by remember { mutableStateOf<Planta?>(null) }
    var plantaIdError by remember { mutableStateOf("") }

    // Estado para la búsqueda de plantas
    var searchQuery by remember { mutableStateOf("") }

    // Estado para almacenar la descripción del proceso inicial
    var descripcionProceso by remember { mutableStateOf("") }
    var descripcionProcesoError by remember { mutableStateOf("") }

    // Obtener la lista de plantas
    val plantas by plantasViewModel.plantasFiltradas.observeAsState(emptyList())
    val isLoading by plantasViewModel.isLoading.observeAsState(false)

    // Cargar la lista de plantas al iniciar
    LaunchedEffect(Unit) {
        plantasViewModel.obtenerPlantas()
    }

    // Actualizar búsqueda cuando cambia searchQuery
    LaunchedEffect(searchQuery) {
        plantasViewModel.setSearchQuery(searchQuery)
    }

    // Observar resultado de creación
    LaunchedEffect(loteViewModel.loteCreado) {
        loteViewModel.loteCreado.value?.let { lote ->
            lote.id?.let { loteId ->
                //Log.d("NAVEGACION", "Navegando a lote/$loteId") // 👈 ¡Añade este log!
                navController.navigate("plantaLista")/* {
                    popUpTo("plantasLista") { inclusive = false }
                }*/
                onDismiss()
            } ?: Log.e("ERROR", "El lote no tiene ID") // 👈 Para debug
        }
    }

    // Diálogo para seleccionar planta
    if (showPlantSelectorDialog) {
        Dialog(onDismissRequest = { showPlantSelectorDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Seleccionar Planta",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Campo de búsqueda
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar plantas...") },
                        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Lista de plantas filtradas
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        items(plantas) { planta ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedPlanta = planta
                                        showPlantSelectorDialog = false
                                        plantaIdError = ""
                                    }
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = planta.nombre ?: "Sin nombre",
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = planta.nombreCientifico ?: "",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                                Text(
                                    text = "ID: ${planta.id}",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Botón para cerrar sin seleccionar
                    Button(
                        onClick = { showPlantSelectorDialog = false },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 8.dp)
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }

    Box {
        Column (
            modifier = Modifier
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Crear lote",
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Por favor introduzca los datos del lote",
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para seleccionar la planta (muestra la seleccionada o mensaje de error)
            OutlinedTextField(
                value = selectedPlanta?.let { "${it.nombre} (ID: ${it.id})" } ?: "",
                onValueChange = { /* No editable directamente */ },
                label = {
                    Text(
                        text = plantaIdError.ifEmpty { "Seleccionar planta madre" },
                        color = if (plantaIdError.isNotEmpty()) Red else Color.Unspecified
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (showPlantSelectorDialog)
                            Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Seleccionar planta",
                        modifier = Modifier.clickable { showPlantSelectorDialog = true }
                    )
                },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 20.dp)
                    .clickable { showPlantSelectorDialog = true },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Campo para la descripción del proceso inicial
            TextField(
                value = descripcionProceso,
                onValueChange = { descripcionProceso = it },
                label = {
                    Text(
                        text = descripcionProcesoError.ifEmpty { "Descripción del proceso inicial" },
                        color = if (descripcionProcesoError.isNotEmpty()) Red else Color.Unspecified
                    )
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Create, contentDescription = "")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row (
                modifier = Modifier.padding(14.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    onClick = onDismiss
                ) {
                    Text("Cancelar")
                }

                Button(
                    modifier = Modifier,
                    onClick = {
                        // Validar campos
                        plantaIdError = if(selectedPlanta == null) "Seleccione una planta madre" else ""
                        descripcionProcesoError = if(descripcionProceso.isBlank()) "Descripción del proceso obligatoria" else ""

                        if (plantaIdError.isEmpty() && descripcionProcesoError.isEmpty()) {
                            // Crear el objeto ProcesoConFecha con la fecha actual
                            val procesoInicial = ProcesoConFecha(
                                descripcion = descripcionProceso,
                                fecha = System.currentTimeMillis()
                            )

                            // Usar la planta seleccionada completa en lugar de crear una nueva
                            // Esto asegura que se envíen todos los campos necesarios de la planta
                            val nuevoLote = Lote(
                                //id = 0, // El backend asignará el ID real
                                planta = Planta(id = selectedPlanta!!.id), // Solo enviamos el ID de la planta
                                procesos = listOf(procesoInicial)
                            )

                            // Llamar al ViewModel para crear el lote
                            loteViewModel.createLote(nuevoLote)
                            navController.navigate("plantasLista")

                        }
                    }
                ) {
                    Text("Crear")
                }
            }
        }
    }
}