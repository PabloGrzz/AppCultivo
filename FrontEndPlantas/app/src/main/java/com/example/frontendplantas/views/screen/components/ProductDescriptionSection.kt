package com.example.frontendplantas.views.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontendplantas.R
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme

@Composable
fun ProductDescriptionSection(
    modifier: Modifier = Modifier,
    productDescription: String
) {

    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(), // Se usa el modifier directamente
        color = ColorGradient1.copy(alpha = 0.5f),
        shape = RoundedCornerShape(32.dp), // Puedes agregar elevation si es necesario
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row (modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    modifier = Modifier.padding(start = 18.dp),
                    text = "Description",
                    style = FrontEndPlantasTheme.typography.titleLarge,
                    color = FrontEndPlantasTheme.colors.onBackground
                )
                ExpandirInfoButton(expanded = expanded,
                    onExpandChange = {expanded = it})
            }
            AnimatedVisibility(
                visible = expanded,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                    Text(
                        text = productDescription,
                        style = FrontEndPlantasTheme.typography.bodyMedium,
                        color = FrontEndPlantasTheme.colors.onBackground,
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        textAlign = TextAlign.Justify
                    )

            }
        }
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
            )
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

