package com.example.frontendplantas.views.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.frontendplantas.models.Planta
import com.example.frontendplantas.ui.theme.Dark
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.ui.theme.White
import com.example.frontendplantas.utils.AuthTokenManager
import com.example.frontendplantas.views.screen.components.FlotanteButton
import com.example.frontendplantas.views.screen.components.HeaderPrincipalSearchBar
import com.example.frontendplantas.views.screen.components.HorizontalScrollBar
import com.example.frontendplantas.views.screen.components.PlantListComponent

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview(){
    var navControler = rememberNavController()

    FrontEndPlantasTheme {
        DashboardScreen(
            Modifier,
            navControler,
            paddingValues = PaddingValues(10.dp)
        )
    }
}

@Composable
fun DashboardScreen(
    modifier: Modifier, navController : NavController,
                    paddingValues : PaddingValues,
                    authTokenManager: AuthTokenManager = AuthTokenManager(LocalContext.current)
){

    // Obtener el rol del usuario y verificar si es admin
    val userRole = authTokenManager.getUserRole()
    val isAdmin = userRole == "ROLE_ADMIN"

    // Estado de desplazamiento para ajustar la sombra dinámicamente
    val scrollState = rememberScrollState()
    val elevation = remember { mutableStateOf(5.dp) }

    // Cambiar la elevación basada en el desplazamiento
    LaunchedEffect(scrollState.value) {
        elevation.value = if (scrollState.value > 0) 10.dp else 5.dp
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = elevation.value, spotColor = Dark)
            ) {
                    HeaderPrincipalSearchBar()
            }
        },
        content = { paddings ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
                    .background(White)
                    .padding(top = 18.dp)
            ) {

                Spacer(modifier.height(12.dp))

                ContenidoDashboard(
                    modifier = Modifier,
                    onPlantClick = { planta ->
                        navController.navigate("plantaDetallesScreen/${planta.id}")
                    }
                )

                FlotanteButton(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 16.dp, start = 16.dp),
                    isAdmin = isAdmin,
                    navController = navController
                )
            }
        }
    )
}

@Composable
fun ContenidoDashboard(
    modifier: Modifier,
    onPlantClick: (Planta) -> Unit
) {
    Column(modifier.padding(horizontal = 12.dp)) {
        HorizontalScrollBar(modifier)
        Spacer(modifier.height(18.dp))
        PlantListComponent(onPlantClick = onPlantClick)
    }
}


