package com.example.frontendplantas.views


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontendplantas.utils.AuthTokenManager
import com.example.frontendplantas.viewmodels.LoteViewModel
import com.example.frontendplantas.viewmodels.PlantasViewModel
import com.example.frontendplantas.views.screen.DashboardScreen
import com.example.frontendplantas.views.screen.DetalleLoteScreen
import com.example.frontendplantas.views.screen.PlantaDetailsScreen
import com.example.frontendplantas.views.screen.components.FormularioNuevaPlanta
import com.example.frontendplantas.views.screen.components.FormularioNuevoLote


@Composable
fun AppNavigator(startDestination: String, paddingValues: PaddingValues) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authTokenManager = remember { AuthTokenManager(context) }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(navController = navController, paddingValues = paddingValues)
        }
        composable("plantasLista") {
            DashboardScreen(
                modifier = Modifier,
                navController = navController,
                paddingValues = paddingValues,
                authTokenManager = authTokenManager
            )
        }
        composable("plantaDetallesScreen/{plantaId}") { backStackEntry ->
            val plantaId = backStackEntry.arguments?.getString("plantaId")?.toLongOrNull()

            if (plantaId != null) {
                PlantaDetailsScreen(
                    plantaId = plantaId,
                    navController = navController,
                    paddingValues = paddingValues,
                    authTokenManager = authTokenManager
                )
            }
        }
        composable("register") {
            RegisterScreen(navController = navController, paddingValues = paddingValues)
        }

        composable("nuevaPlanta") {
            FormularioNuevaPlanta(
                modifier = Modifier,
                navController = navController,
                paddingValues = paddingValues
            )
        }

        composable("nuevoLote") {
            val loteViewModel: LoteViewModel = viewModel()
            val plantasViewModel: PlantasViewModel = viewModel()

            FormularioNuevoLote(
                paddingValues = paddingValues,
                onDismiss = { navController.popBackStack() },
                loteViewModel = loteViewModel,
                plantasViewModel = plantasViewModel,
                navController = navController
            )
        }

        composable("lote/{loteId}") { backStackEntry ->
            val loteId = backStackEntry.arguments?.getString("loteId")?.toLongOrNull()

            if (loteId != null) {
                DetalleLoteScreen(
                    navController = navController,
                    paddingValues = paddingValues,
                    loteId = loteId,
                    authTokenManager = authTokenManager
                )
            }
        }
    }
}
