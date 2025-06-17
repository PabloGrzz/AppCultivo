package com.example.frontendplantas.views.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontendplantas.models.Lote
import com.example.frontendplantas.models.Planta
import com.example.frontendplantas.models.ProcesoConFecha
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme

@Preview(showBackground = true)
@Composable
fun DescripcionLotePreview() {
    // Creamos un objeto Planta primero
    val planta = Planta(
        id = 4L,
        nombre = "Papaya",
        nombreCientifico = "Carica papaya"
    )

    // Usamos la planta en el lote
    val lote = Lote(
        id = 1L,
        numeroLote = 1,
        planta = planta, // Asignamos el objeto planta directamente
        plantaId = planta.id, // También mantenemos la referencia al ID
        procesos = listOf(
            ProcesoConFecha(descripcion = "Siembra", fecha = 1700006400000),
            ProcesoConFecha(descripcion = "Fertilización", fecha = 1701388800000)
        )
    )

    FrontEndPlantasTheme {
        DescripcionLote(lote = lote)
    }
}

@Composable
fun DescripcionLote(lote: Lote) {
    Box {
        BackgroundBox(lote = lote)
    }
}

@Composable
fun BackgroundBox(lote: Lote) {
    var expanded by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.background(
            shape = RoundedCornerShape(16.dp),
            color = ColorGradient1.copy(alpha = 0.3f),
        ).fillMaxWidth()
    ) {
        Column {
            Content(expanded, onExpandChange = { expanded = it })

            HorizontalDivider(thickness = 2.dp, color = Color.Black)

            Box(Modifier.padding(18.dp)) {
                if (expanded) {
                    InformacionProcesos(procesos = lote.procesos)
                }
            }
        }
    }
}

@Composable
fun InformacionProcesos(procesos: List<ProcesoConFecha>) {
    Column {
        Text("Procesos realizados:", style = FrontEndPlantasTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (procesos.isEmpty()) {
            Text("No hay procesos registrados")
        } else {
            procesos.forEach { proceso ->
                Text("(${proceso.getFechaComoString()}) - ${proceso.descripcion}")
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun Content(expanded: Boolean, onExpandChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Detalles del lote",
            fontWeight = FontWeight.Bold
        )

        // Botón de alternancia entre ver procesos
        TextButton(onClick = { onExpandChange(!expanded) }) {
            Text(text = if (expanded) "Ocultar Procesos" else "Ver Procesos")
        }
    }
}