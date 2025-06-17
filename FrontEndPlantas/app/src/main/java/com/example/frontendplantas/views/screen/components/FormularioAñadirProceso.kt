package com.example.frontendplantas.views.screen.components

import android.widget.CalendarView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.frontendplantas.models.ProcesoConFecha
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import java.time.LocalDate

@Preview(showBackground = true)
@Composable
fun FormularioAñadirProcesoPreview(){

    var navController = rememberNavController()

    FrontEndPlantasTheme {
        FormularioAñadirProceso(paddingValues = PaddingValues(), onDismiss = { }, onAddProceso = {},
            loteId = 2
        )
    }
}

@Composable
fun FormularioAñadirProceso (paddingValues : PaddingValues,onDismiss: () -> Unit, onAddProceso: (ProcesoConFecha) -> Unit,loteId: Long){

    var procesos by remember { mutableStateOf("") }
    var procesosError by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    Box(Modifier.padding(10.dp)){
        Column (
            modifier = Modifier
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Añadir proceso a lote",
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 15.dp),
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Porfavor introduzca el nuevo proceso",
                fontSize = 18.sp,
                modifier = Modifier,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeleccionadorCalendario(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                modifier = Modifier.padding(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = procesos,
                onValueChange = { procesos = it },
                label = {
                    Text(
                        //Cabran los procesos en el textField? ->>> AÑADIR FECHA??
                        // ej. Proceso de estabilizacion : 0.8 µg/l Bap,1 g/l carbon activo y 0.4 µg/l Iba
                        text = procesosError.ifEmpty { "Proceso a añadir" },
                        color = if (procesosError.isNotEmpty()) Red else Color.Unspecified
                    )
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Create, contentDescription = "")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent
                )
            )




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
                    onClick = {
                        procesosError = if (procesos.isBlank()) "Proceso obligatorio" else ""

                        if (procesosError.isEmpty()) {
                            // Crear el objeto ProcesoConFecha con la descripción y la fecha seleccionada
                            val procesoConFecha = ProcesoConFecha(
                                descripcion = procesos,
                                fecha = selectedDate  // Esto ahora es un LocalDate
                            )
                            // Llamar a onAddProceso pasando el objeto ProcesoConFecha
                            onAddProceso(procesoConFecha)
                        }
                    }
                ) {
                    Text("Añadir")
                }
            }
        }
    }
}