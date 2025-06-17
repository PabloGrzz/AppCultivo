package com.example.frontendplantas.views.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.frontendplantas.R
import com.example.frontendplantas.models.Lote
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.Dark
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.ui.theme.Pink

@Composable
fun PrincipalLote(lote: Lote) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProductBackground(lote = lote)
    }
}

@Composable
private fun ProductBackground(lote: Lote) {
    Box(
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(bottomEnd = 32.dp, bottomStart = 32.dp),
                color = ColorGradient1.copy(alpha = 0.3f)
            )
    ) {
        Contenido(lote = lote)
    }
}

@Composable
private fun ActionBar(
    modifier: Modifier = Modifier,
    headline: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 50),
        color = Pink
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = headline,
                style = FrontEndPlantasTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Dark
            )
        }
    }
}

@Composable
private fun ActionBarSecundario(
    modifier: Modifier = Modifier,
    headline: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 50),
        color = Pink
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = headline,
                style = FrontEndPlantasTheme.typography.titleSmall,
                color = Color.White
            )
        }
    }
}

@Composable
private fun Contenido(lote: Lote) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (actionBar, actionBar2, productImg) = createRefs()

        // Muestra el número de lote
        ActionBar(
            headline = "Lote ${lote.numeroLote ?: "Sin número"}",
            modifier = Modifier
                .padding(horizontal = 19.dp, vertical = 20.dp)
                .constrainAs(actionBar) {
                    top.linkTo(parent.top)
                }
        )

        // Usa la función segura para obtener el nombre de la planta
        ActionBarSecundario(
            headline = lote.getNombrePlanta(),
            modifier = Modifier
                .padding(horizontal = 19.dp, vertical = 15.dp)
                .constrainAs(actionBar2) {
                    top.linkTo(actionBar.bottom)
                }
        )

        // Aquí se podría mostrar una imagen relacionada con el lote o planta
        Box {
            Image(
                painter = painterResource(id = R.drawable.celosia),
                contentDescription = "Imagen de planta",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .height(200.dp)
                    .padding(start = 180.dp)
            )
        }
    }
}