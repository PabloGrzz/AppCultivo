package com.example.frontendplantas.views.screen

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.frontendplantas.R
import com.example.frontendplantas.data.*
import com.example.frontendplantas.models.Planta
import com.example.frontendplantas.ui.theme.Dark
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.ui.theme.White
import com.example.frontendplantas.utils.AuthTokenManager
import com.example.frontendplantas.viewmodels.PlantasViewModel
import com.example.frontendplantas.views.screen.components.*

private const val TAG = "PlantaDetailsScreen"

@Composable
fun PlantaDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: PlantasViewModel = viewModel(),
    plantaId: Long? = null,
    authTokenManager: AuthTokenManager = AuthTokenManager(LocalContext.current)
) {
    val planta by viewModel.plantaDetalle.observeAsState()
    val scrollState = rememberScrollState()
    val elevation = remember { mutableStateOf(5.dp) }

    // For debugging - let's check the token first
    val token = authTokenManager.getToken()
    Log.d(TAG, "Current token in PlantaDetailsScreen: $token")

    // Get user role and check if admin
    val userRole = authTokenManager.getUserRole()
    Log.d(TAG, "User role in PlantaDetailsScreen: $userRole")

    val isAdmin = userRole == "ROLE_ADMIN"
    Log.d(TAG, "Is admin: $isAdmin")

    // Test with a hardcoded token for debugging if needed
    /*LaunchedEffect(Unit) {
        // Comment out this section after testing
        val testToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInN1YiI6InVzdWFyaW9BZCIsImlhdCI6MTc0NjU3MjM3NywiZXhwIjoxNzQ2NjU4Nzc3fQ.6PgjI9yhdu0iwqM_ja6jwbESWfAI-P0BKvjrfSJkabA"
        Log.d(TAG, "Setting test token for debugging")
        authTokenManager.saveToken(testToken)

        // Verify after saving
        val savedToken = authTokenManager.getToken()
        val roleAfterSave = authTokenManager.getUserRole()
        Log.d(TAG, "After manual token save - Token: $savedToken, Role: $roleAfterSave")
    }*/

    LaunchedEffect(plantaId) {
        plantaId?.let { viewModel.obtenerPlantaPorID(it) }
    }

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
                HeaderPrincipal()
            }
        },
        content = { paddings ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
                    .background(White)
            ) {
                if (planta != null) {
                    Content(
                        modifier = Modifier.padding(top = 0.dp),
                        planta = planta!!,
                        scrollState = scrollState
                    )
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

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
private fun Content(
    modifier: Modifier = Modifier,
    planta: Planta,
    scrollState: ScrollState
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 18.dp, vertical = 10.dp),
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        val productPreviewState = ProductPreviewState.fromPlanta(planta)
        PrincipalPlanta(state = productPreviewState)
        Spacer(modifier = Modifier.height(12.dp))

        PrincipalesCuidados(data = createCuidadosData(planta))
        Spacer(modifier = Modifier.height(16.dp))

        RequerimientosBarra()

        Spacer(modifier = Modifier.height(16.dp))

        CaracteristicasPlanta(
            modifier = Modifier,
            data = convertPlantaToCaracteristicas(planta)
        )
        Spacer(modifier = Modifier.height(16.dp))


        ProductDescriptionSection(
            productDescription = planta.beneficios.ifEmpty { "No hay descripción disponible" },
            modifier = Modifier.navigationBarsPadding()
        )
        Spacer(modifier = Modifier.height(16.dp))

        FertilizantesPlanta(state = convertPlantaToFertilizantes(planta))
        Spacer(modifier = Modifier.height(16.dp))


    }
}

private fun createCuidadosData(planta: Planta): List<ProductFlavorState> {
    return listOf(
        ProductFlavorState(
            name = "Temperatura",
            imgRes = R.drawable.ic_plus,
            value = planta.temperatura
        ),
        ProductFlavorState(
            name = "Riego",
            imgRes = R.drawable.ic_plus,
            value = planta.frecuenciaRiego
        ),
        ProductFlavorState(
            name = "Luz",
            imgRes = R.drawable.ic_plus,
            value = "Adaptable"
        )
    )
}

private fun convertPlantaToCaracteristicas(planta: Planta): List<CaracteristicasPlantaData> {
    return listOf(
        CaracteristicasPlantaData(
            icon = R.drawable.ic_plus,
            title = "Familia",
            value = planta.familia
        ),
        CaracteristicasPlantaData(
            icon = R.drawable.ic_plus,
            title = "Nombre científico",
            value = planta.nombreCientifico ?: ""
        ),
        CaracteristicasPlantaData(
            icon = R.drawable.ic_plus,
            title = "Toxicidad",
            value = planta.toxicidad
        ),
        CaracteristicasPlantaData(
            icon = R.drawable.ic_plus,
            title = "Tipo de suelo",
            value = planta.suelo
        ),
        CaracteristicasPlantaData(
            icon = R.drawable.ic_plus,
            title = "Humedad",
            value = planta.humedad
        ),
        CaracteristicasPlantaData(
            icon = R.drawable.ic_plus,
            title = "Precio",
            value = "${planta.precio}€"
        )
    )
}

private fun convertPlantaToFertilizantes(planta: Planta): ProductNutritionState {
    val nutritionItems = listOf(
        NutritionState("N", "5", "g"),
        NutritionState("P", "3", "g"),
        NutritionState("K", "8", "g")
    )

    return ProductNutritionState(
        title = "Fertilización",
        description = planta.fertilizacion.ifEmpty { "No se especifica fertilización" },
        fertilizantes = Fertilizantes("NPK", ""),
        nutrition = nutritionItems
    )
}

@Preview(showBackground = true)
@Composable
fun PlantaDetailsScreenPreview() {
    FrontEndPlantasTheme {
        PlantaDetailsScreen(
            paddingValues = PaddingValues(0.dp),
            navController = rememberNavController()
        )
    }
}