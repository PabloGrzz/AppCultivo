package com.example.frontendplantas.models

import androidx.annotation.DrawableRes
import com.example.frontendplantas.R


sealed class OnboardingModel(
    @DrawableRes
    val image: Int,
    val title : String,
    val description : String
) {

    data object FirstPages : OnboardingModel(
        image = R.drawable.img_into_1,
        title = "Titulo1",
        description = "Descripcion 1"
    )
    data object SecondPages : OnboardingModel(
        image = R.drawable.img_into_2,
        title = "Titulo2",
        description = "Descripcion 2"
    )
    data object ThirdPages : OnboardingModel(
        image = R.drawable.img_into_3,
        title = "Titulo3",
        description = "Descripcion 3"
    )



}