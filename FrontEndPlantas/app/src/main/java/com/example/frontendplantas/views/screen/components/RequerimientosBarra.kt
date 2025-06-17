package com.example.frontendplantas.views.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.frontendplantas.R
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.Dark
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.ui.theme.Pink
import com.example.frontendplantas.ui.theme.Pink1

@Preview(showBackground = true)
@Composable
fun RequerimientosBarraPreview(){
    RequerimientosBarra()
}

@Composable
fun RequerimientosBarra(){

    Column {

    //SectionHeader(modifier = Modifier,"Principales requerimientos","\uD83C\uDF31")

    //Spacer(modifier = Modifier.height(16.dp))


    Surface (modifier = Modifier,
        color = ColorGradient1.copy(alpha = 0.5f),
        shape = RoundedCornerShape(32.dp)) {

        /*modifier: Modifier = Modifier,
        title: String,
        emotion: String*/


        //
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Indicador de sol
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

                Image(
                    painter = painterResource(id = R.drawable.solazo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit, // Ajusta la imagen al espacio fijo "Fit" para escalarla y no recortar
                    modifier = Modifier
                        .size(50.dp) // Tamaño fijo para la imagen
                )

                Spacer(modifier = Modifier.width(16.dp))

                Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                    LevelIndicator(
                        level = 0.7f, // 70% aqui pasamos el dato de la planta

                        indicatorColor = Color.Yellow
                    )
                }
            }

            // Indicador de agua
            Row {
                Image(
                    painter = painterResource(id = R.drawable.gota4),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(50.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                    LevelIndicator(
                        level = 0.4f, // 40%
                        indicatorColor = Color.Blue
                    )
                }
            }

            // Indicador de temperatura
            Row  {
                Image(
                    painter = painterResource(id = R.drawable.temperatura2),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(50.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                    LevelIndicator(
                        level = 0.5f, // 50%
                        indicatorColor = Color.Red
                    )
                }
            }
        }
    }
    }
}

@Composable
fun LevelIndicator(
    level: Float, // Valor entre 0 y 1
    //labels: List<String> = listOf("Poco", "Normal", "Mucho"),
    //barColor: Color = Color.LightGray,
    indicatorColor: Color = Color.Blue,
    barHeight: Dp = 10.dp,
    indicatorRadius: Dp = 10.dp
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val width = maxWidth
        val labelPadding = 4.dp

        // Barra de fondo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .clip(RoundedCornerShape(barHeight / 2))
                .background(Pink1.copy(alpha = 0.8f))
                .align(Alignment.CenterStart)
        )

        // Indicador
        Box(
            modifier = Modifier
                .size(indicatorRadius * 2)
                .offset(x = width * level - indicatorRadius)
                .background(Dark, CircleShape)
                .align(Alignment.CenterStart)
        )

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


