package com.example.frontendplantas.views.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.frontendplantas.R
import com.example.frontendplantas.data.ProductHighlightState
import com.example.frontendplantas.data.ProductHighlightType
import com.example.frontendplantas.data.ProductPreviewState
import com.example.frontendplantas.ui.theme.FrontEndPlantasTheme
import com.example.frontendplantas.ui.theme.ColorGradient1
import com.example.frontendplantas.ui.theme.ColorGradient2
import com.example.frontendplantas.ui.theme.ColorGradient3
import com.example.frontendplantas.ui.theme.Dark

@Preview(showBackground = true)
@Composable
fun ProductPreviewSectionPreview() {
    FrontEndPlantasTheme {
        PrincipalPlanta(
            modifier = Modifier,
            state = ProductPreviewState(
                headline = "Planta de Ejemplo",
                productImg = R.drawable.celosia, // Asegúrate de tener este recurso
                highlights = listOf(
                    ProductHighlightState("Familia", ProductHighlightType.SECONDARY),
                    ProductHighlightState("Nombre científico", ProductHighlightType.SECONDARY),
                    ProductHighlightState("Precio: 15.99€", ProductHighlightType.PRIMARY)
                )
            )
        )
    }
}


@Composable
fun PrincipalPlanta(
    modifier: Modifier = Modifier,
    state: ProductPreviewState,
) {
    var popupManager by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.height(IntrinsicSize.Max)
    ) {
        ProductBackground(
            Modifier.padding(bottom = 24.dp)
        )
        Content(
            state = state,
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 24.dp)
        )

        /*Row (modifier = Modifier.align(Alignment.BottomStart)){

            //CloseButton(modifier.padding(start = 28.dp, bottom = 10.dp),popupManager)

            Button(
                onClick = {
                    //crea una funcion en el hijo para que tal // eres gilipollas manda tut a un viewModel
                    popupManager = true

                },
                modifier.padding(start = 28.dp, bottom = 10.dp)
            ) {

                Text("Añadir lote")
            }

            Spacer(Modifier.width(16.dp))
        }

        AnimatedVisibility(
            visible = popupManager,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 8.dp
                ) {
                    FormularioNuevoLote(
                        paddingValues = PaddingValues(0.dp),
                        onDismiss = { popupManager = false }
                    )
                }

            }
        }*/


    }
}

@Composable
private fun ProductBackground(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    0f to ColorGradient1,
                    0.5f to ColorGradient2,
                    1f to ColorGradient3
                ),
                //color = AppTheme.colors.secondarySurface,
                shape = RoundedCornerShape(32.dp)
            )
    )/*{

    //asi es como divides un box en dos y le añadaes 1 color a cada (Para el Anuncio debajo del dash board)
        Box (modifier.fillMaxSize().width(100.dp).background(Color.Red))

        Box(modifier.fillMaxHeight().fillMaxSize(0.3f).align(Alignment.CenterEnd).background(Color.Blue))

    }*/
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    state: ProductPreviewState
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (actionBar, highlights, productImg) = createRefs()

        ActionBar(
            headline = state.headline ?: "",
            modifier = Modifier
                .padding(horizontal = 19.dp)
                .constrainAs(actionBar) {
                    top.linkTo(parent.top)
                }
        )

        Image(
            painter = painterResource(id = state.productImg),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .height(256.dp)
                .constrainAs(productImg) {
                    end.linkTo(parent.end)
                    top.linkTo(anchor = actionBar.bottom, margin = 20.dp)
                }
        )

        ProductHighlights(
            highlights = state.highlights,
            modifier = Modifier.constrainAs(highlights) {
                start.linkTo(anchor = parent.start, margin = 19.dp)
                top.linkTo(productImg.top)
            }
        )

    }
}

@Composable
private fun ActionBar(
    modifier: Modifier = Modifier,
    headline: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = headline,
            style = MaterialTheme.typography.headlineLarge,
            color = Dark
        )
        //CloseButton()
    }
}

/*@Composable
private fun CloseButton(
    modifier: Modifier = Modifier,
    popupManager : Boolean,
) {
            Button(
                onClick = {
                    popupManager = true

            },
                modifier.padding(start = 8.dp)
            ) {

                Text("Añadir lote")
            }


}*/
