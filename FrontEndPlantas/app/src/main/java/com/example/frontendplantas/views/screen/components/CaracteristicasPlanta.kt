package com.example.frontendplantas.views.screen.components

import android.provider.CalendarContract.Colors
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontendplantas.R
import com.example.frontendplantas.data.CaracteristicasPlantaDatas
import com.example.frontendplantas.data.CaracteristicasPlantaData
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.ColorGradient3
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme

@Preview(showBackground = true)
@Composable
fun CaracteristicasPlantasPreview() {
    FrontEndPlantasTheme {
        CaracteristicasPlanta(
            modifier = Modifier.fillMaxWidth(),
            data = CaracteristicasPlantaDatas
        )
    }
}

@Composable
fun CaracteristicasPlanta(
    modifier: Modifier = Modifier,
    data: List<CaracteristicasPlantaData> = CaracteristicasPlantaDatas
) {

    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = ColorGradient1.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 18.dp,
                horizontal = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CaracteristicasPlantaHeader(
                expanded = expanded,
                onExpandChange = {expanded = it}
            )
            //GridStatic(data=data)
            AnimatedVisibility(
                visible = expanded,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                GridStatic(data=data)
            }

            }
    }
}

@Composable
private fun CaracteristicasPlantaHeader(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.forest_outline),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = FrontEndPlantasTheme.colors.onBackground
            )
            Text(
                text = "Caracteristicas Completas",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp
                ),
                color = FrontEndPlantasTheme.colors.onBackground
            )
        }
        ExpandirInfoButton(
            expanded = expanded,
            onExpandChange = onExpandChange)
    }
}

@Composable
private fun ExpandirInfoButton(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit
) {

    Surface(
        shape = CircleShape,
        modifier = modifier
            .size(32.dp)
            .shadow(
                elevation = 4.dp,
                shape = CircleShape
            ),
        

    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = {
                run { onExpandChange(!expanded) }
            }) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_down_thick),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                        .rotate(if (expanded) 180f else 0f),  // Rota el ícono 180° si está expandido
                )
            }
        }
    }
}


@Composable
private fun CaracteristicasPlantasDashboard(
    modifier: Modifier = Modifier,
    data: CaracteristicasPlantaData
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(data.icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = FrontEndPlantasTheme.colors.onBackground
        )
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = data.title,
                style = MaterialTheme.typography.labelSmall,
                color = FrontEndPlantasTheme.colors.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = data.value,
                style = MaterialTheme.typography.labelSmall,
                color = FrontEndPlantasTheme.colors.onBackground
            )
        }
    }
}

@Composable
private fun GridStatic(
    data: List<CaracteristicasPlantaData>
) {
    // Dividimos la lista en grupos de 3 elementos (para 3 columnas)
    val rows = data.chunked(3)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { item ->
                    CaracteristicasPlantasDashboard(
                        data = item,
                        modifier = Modifier.weight(1f) // Cada ítem ocupa el mismo espacio
                    )
                }
                // Si la fila no tiene 3 elementos, agregamos espacios vacíos
                if (row.size < 3) {
                    for (i in 0 until (3 - row.size)) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}