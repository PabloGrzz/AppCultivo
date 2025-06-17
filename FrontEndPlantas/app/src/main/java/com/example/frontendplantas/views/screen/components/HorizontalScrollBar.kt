package com.example.frontendplantas.views.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.Dark
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.ui.theme.Pink


@Preview(showBackground = true)
@Composable
fun HorizontalScrollBarPreview(){
    HorizontalScrollBar(modifier = Modifier.fillMaxSize())
}



@Composable
fun HorizontalScrollBar(modifier: Modifier){

    val compatibility_list : List<String> = listOf("calathea","maranta","philodendron","musa","croton")

    LazyRow (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ){

        //simulo los datos, tengo q hacer en la base de datos un campo compatibilidad para mostrar compatibles?? veremows

        items(compatibility_list) { item ->

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorGradient1.copy(alpha = 0.5f), // Color de fondo del botón
                    contentColor = Dark // Color del texto del botón
                ),
                modifier = Modifier.height(30.dp).wrapContentWidth()
            ) {
                Text(text = item, style = FrontEndPlantasTheme.typography.bodySmall, textAlign = TextAlign.Center)
            }
        }
    }

}

