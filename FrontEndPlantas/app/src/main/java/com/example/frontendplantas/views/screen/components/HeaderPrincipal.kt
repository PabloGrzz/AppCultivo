package com.example.frontendplantas.views.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendplantas.R
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.ColorGradient2
import com.example.frontendplantas.ui.theme.ColorGradient3
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.ui.theme.Pink
import com.example.frontendplantas.ui.theme.Pink1
import com.example.frontendplantas.ui.theme.YellowLight
import com.example.frontendplantas.viewmodels.PlantasViewModel

@Preview(showBackground = true)
@Composable
fun HeaderPrincipalPreview() {
    //HeaderPrincipal(Modifier)
    HeaderPrincipalSearchBar(Modifier)

}



@Composable
fun HeaderPrincipal(
    modifier: Modifier = Modifier
) {
    // Usamos Surface para envolver el Row y aplicar el shadow de manera controlada
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp), // Sombra en el Surface
        color = FrontEndPlantasTheme.colors.surface // Puede ser el color de fondo deseado
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp, bottom = 10.dp, start = 6.dp, end = 6.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ControlButton()
            LocationInfo(
                modifier = Modifier.padding(top = 10.dp),
                location = "Luna Creciente"
            )
            ProfileButton()
        }
    }
}

@Composable
fun HeaderPrincipalSearchBar(
    modifier: Modifier = Modifier
) {
    // Usamos Surface para envolver el Row y aplicar el shadow de manera controlada
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp), // Sombra en el Surface
        color = FrontEndPlantasTheme.colors.surface // Puede ser el color de fondo deseado
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp, bottom = 10.dp, start = 6.dp, end = 6.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ControlButton()
                LocationInfo(
                    modifier = Modifier.padding(top = 10.dp),
                    location = "Luna Creciente"
                )
                ProfileButton()
            }

            SearchBar()

            Spacer(modifier.height(10.dp))
        }
    }
}

@Composable
private fun ControlButton(
    modifier: Modifier = Modifier
) {
    Surface(
        color = FrontEndPlantasTheme.colors.surface,
        shape = CircleShape,
        modifier = modifier
            .size(48.dp)
            .shadow(
                elevation = 4.dp, // Cambiado a shadow de Compose
                shape = CircleShape
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_control), // Asegúrate de que este recurso exista
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ProfileButton(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .border(
                width = 1.5.dp,
                color = FrontEndPlantasTheme.colors.regularSurface,
                shape = CircleShape
            )
            .shadow(
                elevation = 6.dp, // Cambiado a shadow de Compose
                shape = CircleShape
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_profile),
            contentDescription = null,
            modifier = modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
    }
}

@Composable
private fun LocationInfo(
    modifier: Modifier = Modifier,
    location: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.moon_waxing_crescent),
                contentDescription = null,
                modifier = Modifier.height(18.dp),
                contentScale = ContentScale.FillHeight
            )
            Text(
                text = location,
                style = MaterialTheme.typography.titleLarge,
                color = FrontEndPlantasTheme.colors.onBackground, // Cambiado a onBackground
                fontWeight = FontWeight.Bold
            )
        }
        ProgressBar()


    }
}

@Composable
private fun ProgressBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = ColorGradient1.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                vertical = 2.dp,
                horizontal = 10.dp)
    ) {
        Text(
            text = "• Esquejes •",
            style = MaterialTheme.typography.labelSmall,
            color = FrontEndPlantasTheme.colors.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun SearchBar(viewModel: PlantasViewModel = viewModel()) {
    val queryState = viewModel.searchQuery.observeAsState("")
    val query = queryState.value

    TextField(
        value = query,
        onValueChange = { viewModel.setSearchQuery(it) },
        label = { Text("Planta") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(32.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = ColorGradient1.copy(alpha = 0.3f),
            focusedContainerColor = ColorGradient1.copy(alpha = 0.5f),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onSearch = { /* cerrar teclado si querés */ })
    )
}

