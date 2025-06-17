package com.example.frontendplantas.views.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontendplantas.data.Fertilizantes
import com.example.frontendplantas.data.NutritionState
import com.example.frontendplantas.data.ProductNutritionState
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.data.ProductNutritionData
import com.example.frontendplantas.ui.theme.ColorGradient1

@Preview(showBackground = true)
@Composable
fun FertilizantesPlantaPreview(){
    FertilizantesPlanta(Modifier, state = ProductNutritionData)
}

@Composable
fun FertilizantesPlanta(
    modifier: Modifier = Modifier,
    state: ProductNutritionState
) {

    Surface(
        modifier = Modifier.fillMaxWidth()
            ,
        color = ColorGradient1.copy(alpha = 0.5f),
        shape = RoundedCornerShape(32.dp)
    //elevation = 4.dp -> para crear sombra
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
                .padding(vertical = 18.dp,
                    horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader(
                title = "Fertilizantes",
                fertilizantes = state.fertilizantes
            )


                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        state.nutrition.onEach { item -> NutritionItem(state = item) }
                    }
        }
    }

}

@Composable
private fun SectionHeader(
    modifier: Modifier = Modifier,
    title: String,
    fertilizantes: Fertilizantes
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = FrontEndPlantasTheme.typography.titleLarge,
            color = FrontEndPlantasTheme.colors.onBackground
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = fertilizantes.value,
                style = FrontEndPlantasTheme.typography.titleMedium,
                color = FrontEndPlantasTheme.colors.onBackground
            )

            Text(
                text = fertilizantes.unit,
                style = FrontEndPlantasTheme.typography.titleMedium,
                color = FrontEndPlantasTheme.colors.onBackground
            )
        }
    }
}

@Composable
private fun NutritionItem(
    modifier: Modifier = Modifier,
    state: NutritionState
) {

    Surface(
        shape = RoundedCornerShape(50),
        color = ColorGradient1.copy(alpha = 0.9f)
    ) {
        Column(
            modifier = modifier.padding(8.dp).size(80.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = state.amount,
                    style = FrontEndPlantasTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light,
                    color = FrontEndPlantasTheme.colors.onBackground
                )
                Text(
                    text = state.unit,
                    style = FrontEndPlantasTheme.typography.titleMedium,
                    color = FrontEndPlantasTheme.colors.onBackground,
                    fontWeight = FontWeight.Light
                )
            }
            Text(
                text = state.title,
                style = FrontEndPlantasTheme.typography.labelMedium,
                color = FrontEndPlantasTheme.colors.onBackground
            )
        }
    }
}