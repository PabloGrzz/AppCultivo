package com.example.frontendplantas.views.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontendplantas.data.ProductFlavorState
import com.example.frontendplantas.data.ProductFlavorsData
import com.example.frontendplantas.data.ProductPreviewState
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme

@Preview(showBackground = true)
@Composable
fun PrincipalesCuidadosPreview(){
    PrincipalesCuidados(modifier = Modifier, data = ProductFlavorsData)
}


@Composable
fun PrincipalesCuidados(
    modifier: Modifier = Modifier,
    data: List<ProductFlavorState>
) {
    Column{
        SectionHeader(
            title = "Principales requerimientos",
            emotion = "\uD83C\uDF31"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            data.onEach { item ->
                ProductFlavorItem(
                    state = item,
                    modifier = Modifier.weight(weight = 1f)
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    modifier: Modifier = Modifier,
    title: String,
    emotion: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = FrontEndPlantasTheme.colors.onBackground
        )
        Text(
            text = emotion,
            style = FrontEndPlantasTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ProductFlavorItem(
    modifier: Modifier = Modifier,
    state: ProductFlavorState
) {
    Box(
        modifier = modifier//.size(120.dp)

            .background(
                shape = RoundedCornerShape(28.dp),
                color = ColorGradient1.copy(alpha = 0.5f)
            )
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 10.dp, //tenia 20.dp
                horizontal = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = state.imgRes),
                contentDescription = null,
                contentScale = ContentScale.Fit, // Ajusta la imagen al espacio fijo "Fit" para escalarla y no recortar
                modifier = Modifier
                    .size(100.dp) // Tamaño fijo para la imagen
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.name,
                    style = FrontEndPlantasTheme.typography.bodyMedium,
                    color = FrontEndPlantasTheme.colors.onRegularSurface,
                    textAlign = TextAlign.Center
                )
                /*Text( Por si quieres meter otro dato
                    text = "+${state.otraInfo}",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.onRegularSurface
                )*/
            }
        }
    }
}